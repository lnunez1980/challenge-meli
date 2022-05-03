package com.challenge.meli.ui.fragments.views

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View.OnKeyListener
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import androidx.core.widget.doOnTextChanged
import com.challenge.meli.R
import com.challenge.meli.helpers.setDrawableLeft


class MeliSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    interface SearchViewListener {
        fun onSearch(search: String)
        fun onFilterRecentSearches(search: String) {}
        fun onEmptySearch() {}
    }

    private var listener: SearchViewListener? = null

    init {
        setupStyle()
        isSingleLine = true
        doOnTextChanged { text, _, _, _ ->
            error = null
            if (text.toString().isNotEmpty()) {
                listener?.onFilterRecentSearches(text.toString())
            }
        }
    }

    fun setListener(listener: SearchViewListener) {
        this.listener = listener
        setOnKeyListener(OnKeyListener { _, keyCode, event ->
            when {
                isKeyCodeEnterPressed(event, keyCode, !text.isNullOrEmpty()) -> {
                    listener.onSearch(text.toString())
                    return@OnKeyListener true
                }
                isKeyCodeEnterPressed(event, keyCode, text.isNullOrEmpty()) -> {
                    error = resources.getString(R.string.search_view_error)
                    return@OnKeyListener true
                }
                isKeyCodeDelPressed(event, keyCode) -> {
                    listener.onEmptySearch()
                    return@OnKeyListener true
                }
            }
            false
        })
    }

    private fun setupStyle() {
        setHint()
        setPadding(12)
        setDrawableLeft(
            ResourcesCompat.getDrawable(resources, R.drawable.ic_search, null),
            ResourcesCompat.getColor(resources, R.color.grey, null)
        )
        setRoundedBackground()
    }

    private fun setHint() {
        setTextColor(ResourcesCompat.getColor(resources, R.color.grey, null))
        setHintTextColor(ResourcesCompat.getColor(resources, R.color.grey, null))
        hint = resources.getString(R.string.search_hint)
    }

    private fun setRoundedBackground() {
        background = ContextCompat.getDrawable(context, R.drawable.rounded_corner)
    }

    private fun isKeyCodeEnterPressed(event: KeyEvent, keyCode: Int, isEmpty: Boolean) =
        event.action == KeyEvent.ACTION_DOWN &&
                keyCode == KeyEvent.KEYCODE_ENTER &&
                isEmpty

    private fun isKeyCodeDelPressed(event: KeyEvent, keyCode: Int) =
        text.toString().isEmpty() &&
                event.action == KeyEvent.ACTION_UP &&
                keyCode == KeyEvent.KEYCODE_DEL
}