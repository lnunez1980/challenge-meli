package com.challenge.meli.ui.fragments.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import androidx.core.widget.doOnTextChanged
import com.challenge.meli.R
import com.challenge.meli.helpers.setDrawableLeft
import com.challenge.meli.helpers.setDrawableRight

const val DRAWABLE_RIGHT = 2

@SuppressLint("ClickableViewAccessibility")
class MeliSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    interface SearchViewListener {
        /**
         * event called when press enter
         * @param search search word
         */
        fun onSearch(search: String)

        /**
         * event is called each time a key is pressed to filter recent searches
         * @param search search word
         */
        fun onFilterRecentSearches(search: String) {}

        /**
         * event is called when the search field is empty
         */
        fun onEmptySearchByFilter() {}
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
            setIconClear()
        }
        setupListenerClearIcon()
    }

    private fun setupListenerClearIcon() {
        setOnTouchListener { _, event ->
            compoundDrawables[DRAWABLE_RIGHT]?.let {
                if (event.rawX >= right - it.bounds.width()) {
                    setText("")
                    listener?.onEmptySearchByFilter()
                }
            }
            false
        }
    }

    fun setListener(listener: SearchViewListener) {
        this.listener = listener
        setOnKeyListener { _, keyCode, event ->
            when {
                isKeyCodeEnterPressed(event, keyCode, !text.isNullOrEmpty()) -> {
                    listener.onSearch(text.toString())
                }
                isKeyCodeEnterPressed(event, keyCode, text.isNullOrEmpty()) -> {
                    error = resources.getString(R.string.search_view_error)
                }
                isKeyCodeDelPressed(event, keyCode) -> {
                    listener.onEmptySearchByFilter()
                }
            }
            false
        }
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
        text.toString().length <= 1 &&
                event.action == KeyEvent.ACTION_DOWN &&
                keyCode == KeyEvent.KEYCODE_DEL

    private fun setIconClear() {
        if (!text.isNullOrEmpty()) {
            setDrawableRight(
                ResourcesCompat.getDrawable(resources, R.drawable.ic_cancel, null),
                ResourcesCompat.getColor(resources, R.color.grey, null)
            )
        } else {
            setCompoundDrawables(null, null, null, null)
            setDrawableLeft(
                ResourcesCompat.getDrawable(resources, R.drawable.ic_search, null),
                ResourcesCompat.getColor(resources, R.color.grey, null)
            )
        }
    }


}