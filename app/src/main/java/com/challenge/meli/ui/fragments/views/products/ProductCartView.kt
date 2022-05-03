package com.challenge.meli.ui.fragments.views.products

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.bumptech.glide.Glide
import com.challenge.meli.R
import com.challenge.meli.databinding.ProductCartViewBinding
import com.challenge.meli.helpers.addRippleForeground
import com.challenge.meli.helpers.priceToString
import com.challenge.meli.data.models.Product

const val NEW: String = "new"

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ProductCartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {


    private val binding = ProductCartViewBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    private lateinit var product: Product

    init {
        addRippleForeground()
    }

    @ModelProp
    fun setData(data: Product) {
        product = data
    }

    @CallbackProp
    fun setListener(listener: ProductCartListener?) {
        binding.cardView.setOnClickListener {
            listener?.onProductClicked(product)
        }
    }

    @AfterPropsSet
    fun after() {
        binding.productNameTextView.text = product.title
        binding.productPriceTextView.text = product.price.priceToString()

        binding.productConditionTextView.text = if (product.condition == NEW) {
            resources.getString(R.string.product_card_new_condition)
        } else {
            resources.getString(R.string.product_card_used_condition)
        }

        binding.productFreeShippingTextView.text =
            resources.getString(R.string.product_card_free_shipping)
        binding.productFreeShippingTextView.isVisible = product.shipping.freeShipping == true

        binding.productSoldTextView.text =
            resources.getString(R.string.product_card_sold, product.soldQuantity.toString())

        Glide.with(context)
            .load(product.thumbnail)
            .fitCenter()
            .into(binding.productImageview)
    }

}