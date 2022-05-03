package com.challenge.meli.ui.fragments.views.search

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.challenge.meli.databinding.RecentSearchLoaderViewBinding
import android.view.LayoutInflater

class RecentSearchLoaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = RecentSearchLoaderViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {
        binding.animationView.playAnimation()
    }
}