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
import com.challenge.meli.data.models.Product
import com.challenge.meli.databinding.SearchFragmentBinding
import com.challenge.meli.ui.fragments.controllers.SearchAdapterController
import com.challenge.meli.ui.fragments.viewModels.SearchViewModel
import com.challenge.meli.ui.fragments.viewModels.SearchViewModel.SearchActions.ProductSearchFound
import com.challenge.meli.ui.fragments.viewModels.SearchViewModel.SearchActions.SearchLoading
import com.challenge.meli.ui.fragments.views.MeliSearchView
import com.challenge.meli.ui.fragments.views.products.ProductCartListener
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class SearchFragment : Fragment(), ProductCartListener, MeliSearchView.SearchViewListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SearchViewModel

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchAdapterController: SearchAdapterController

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupViewModel()
        setupSearchView()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onSearch(search: String) {
        searchAdapterController.clear()
        viewModel.getSearch(search)
    }

    override fun onProductClicked(product: Product) {
        addSearchFragment(product)
    }

    private fun addSearchFragment(product: Product) {
        val action = SearchFragmentDirections.toProductDetailFragment(product)
        findNavController().navigate(action)
    }

    private fun setupSearchView() {
        binding.searchView.setListener(this)
        binding.searchView.setText(viewModel.querySearch)
    }

    private fun setupRecyclerView() {
        searchAdapterController = SearchAdapterController(this@SearchFragment)
        binding.recycleView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recycleView.addItemDecoration(
            EpoxyItemSpacingDecorator(resources.getDimensionPixelSize(R.dimen.space_4))
        )
        binding.recycleView.setHasFixedSize(false)
        binding.recycleView.setController(searchAdapterController)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]
        lifecycle.addObserver(viewModel)
        listenToObservers()
    }

    private fun setData(product: List<Product>) {
        if (product.isNotEmpty()) {
            searchAdapterController.dispatch(product)
        } else {
            binding.emptyStateView.setMessage(
                getString(R.string.search_empty_state_title),
                getString(R.string.search_empty_state_sub_title)
            )
        }
        binding.emptyStateView.isVisible = product.isEmpty()
    }

    private fun listenToObservers() {
        viewModel.searchActions.observe(viewLifecycleOwner) { actions ->
            when (actions) {
                is SearchLoading -> binding.loaderView.isVisible = actions.loading
                is ProductSearchFound -> setData(actions.products)
                is SearchViewModel.SearchActions.OnError -> {
                    actions.error.printStackTrace()
                    Snackbar.make(
                        this.requireView(),
                        requireContext().getString(R.string.error_message),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    requireActivity().onBackPressed()
                }
            }
        }
    }
}