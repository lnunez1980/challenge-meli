package com.challenge.meli.ui.fragments.views.search

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.challenge.meli.databinding.RecentSearchItemViewBinding
import com.challenge.meli.helpers.addRippleForeground

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class RecentSearchItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = RecentSearchItemViewBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    private var textItem: String = ""

    init {
        addRippleForeground()
    }

    @ModelProp
    fun setTextItem(text: String) {
        textItem = text
    }

    @CallbackProp
    fun setListener(listener: RecentSearchListener?) {
        setOnClickListener {
            listener?.onRecentSearchClicked(textItem)
        }
    }

    @AfterPropsSet
    fun after() {
        binding.nameTextView.text = textItem
    }

}