package com.challenge.meli.ui.fragments.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.challenge.meli.databinding.ProductDetailAttributeViewBinding
import com.challenge.meli.data.models.Attribute

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class AttributeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private lateinit var data: Attribute

    private val binding = ProductDetailAttributeViewBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    @ModelProp
    fun setData(data: Attribute) {
        this.data = data
    }

    @AfterPropsSet
    fun bindData() {
        binding.textViewKey.text = data.name
        binding.textViewValue.text = data.valueName
    }
}