package com.challenge.meli.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyItemSpacingDecorator
import com.challenge.meli.R
import com.challenge.meli.data.database.models.RecentSearches
import com.challenge.meli.databinding.RecentSearchFragmentBinding
import com.challenge.meli.ui.fragments.controllers.RecentSearchAdapterController
import com.challenge.meli.ui.fragments.viewModels.RecentSearchViewModel
import com.challenge.meli.ui.fragments.viewModels.RecentSearchViewModel.RecentSearchActions.RecentSearchFound
import com.challenge.meli.ui.fragments.viewModels.RecentSearchViewModel.RecentSearchActions.RecentSearchLoading
import com.challenge.meli.ui.fragments.views.MeliSearchView
import com.challenge.meli.ui.fragments.views.search.RecentSearchListener
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class RecentSearchFragment : Fragment(), RecentSearchListener, MeliSearchView.SearchViewListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: RecentSearchViewModel

    private var _binding: RecentSearchFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var recentSearchAdapterController: RecentSearchAdapterController

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecentSearchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupRecyclerView()
        binding.searchView.setListener(this)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onRecentSearchClicked(recentSearch: String) {
        processRecentSearch(recentSearch)
    }

    override fun onSearch(search: String) {
        processRecentSearch(search)
    }

    override fun onFilterRecentSearches(search: String) {
        viewModel.filterRecentSearches(search)
    }

    private fun processRecentSearch(search: String) {
        viewModel.addRecentSearch(search)
        addSearchFragment(search)
    }

    private fun addSearchFragment(query: String) {
        val action = RecentSearchFragmentDirections.toSearchFragment(query)
        findNavController().navigate(action)
    }

    private fun setupRecyclerView() {
        recentSearchAdapterController = RecentSearchAdapterController(this@RecentSearchFragment)
        binding.recycleView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recycleView.addItemDecoration(EpoxyItemSpacingDecorator(4))
        binding.recycleView.setHasFixedSize(false)
        binding.recycleView.setController(recentSearchAdapterController)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[RecentSearchViewModel::class.java]
        lifecycle.addObserver(viewModel)
        listenToObservers()
    }

    private fun listenToObservers() {
        viewModel.recentSearchActions.observe(viewLifecycleOwner) { actions ->
            when (actions) {
                is RecentSearchLoading -> binding.loaderView.isVisible = actions.loading
                is RecentSearchFound -> setData(actions.recentSearches)
                is RecentSearchViewModel.RecentSearchActions.OnError -> {
                    actions.error.printStackTrace()
                    Snackbar.make(
                        this.requireView(),
                        requireContext().getString(R.string.error_message),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setData(recentSearches: List<RecentSearches>) {
        if (recentSearches.isNotEmpty()) {
            recentSearchAdapterController.dispatch(recentSearches)
        } else {
            binding.emptyStateView.isVisible = true
        }
    }
}