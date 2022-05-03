package com.challenge.meli.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.challenge.meli.R
import com.challenge.meli.databinding.ProductDetailFragmentBinding
import com.challenge.meli.helpers.priceToString
import com.challenge.meli.data.models.Address
import com.challenge.meli.data.models.Attribute
import com.challenge.meli.data.models.Product
import com.challenge.meli.ui.fragments.viewModels.ProductDetailViewModel
import com.challenge.meli.ui.fragments.views.attributeView
import dagger.android.support.AndroidSupportInjection
import java.util.Locale
import javax.inject.Inject

class ProductDetailFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ProductDetailViewModel

    private var _binding: ProductDetailFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProductDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupViewModel()
        binding.backButton.setOnClickListener { requireActivity().onBackPressed() }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[ProductDetailViewModel::class.java]
        lifecycle.addObserver(viewModel)
        viewModel.product.observe(viewLifecycleOwner) { product ->
            bindProductDetailHeader(product)
        }
    }

    private fun bindProductDetailHeader(product: Product) {
        binding.textViewConditions.text = getString(
            R.string.product_detail_conditions,
            product.condition.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            },
            product.soldQuantity.toString()
        )
        binding.textViewTitle.text = product.title
        binding.textViewPrice.text = product.price.priceToString()
        binding.textViewSoldBy.text = Html.fromHtml(
            getString(R.string.product_detail_sold_by, product.domainId),
            Html.FROM_HTML_MODE_COMPACT
        )
        bindAddress(product.address)
        Glide.with(this)
            .load(product.thumbnail)
            .into(binding.imageViewPhoto)
        addAttributes(product.attributes)
    }

    private fun bindAddress(address: Address?) {
        address?.let {
            binding.textViewLocationValue.text = getString(
                R.string.product_detail_location_value,
                it.cityName,
                it.stateName
            )
        } ?: kotlin.run {
            binding.textViewLocationValue.visibility = GONE
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.setHasFixedSize(false)
    }

    private fun addAttributes(attributes: List<Attribute>) {
        binding.recyclerView.withModels {
            attributes
                .filter { it.name.isNotBlank() && !it.valueName.isNullOrBlank() }
                .forEach { attribute ->
                    attributeView {
                        id(attribute.id)
                        data(attribute)
                    }
                }
        }
    }
}