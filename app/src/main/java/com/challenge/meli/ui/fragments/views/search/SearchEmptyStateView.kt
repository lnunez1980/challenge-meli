package com.challenge.meli.ui.fragments.views.search

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.LayoutInflater
import com.challenge.meli.R
import com.challenge.meli.databinding.RecentSearchEmptyStateViewBinding

class SearchEmptyStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = RecentSearchEmptyStateViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {
        setMessage()
    }

    fun setMessage(
        title: String = resources.getString(R.string.empty_state_title),
        subtitle: String = resources.getString(R.string.empty_state_sub_title)
    ) {
        binding.titleTextView.text = title
        binding.subTitleTextView.text = subtitle
    }
}