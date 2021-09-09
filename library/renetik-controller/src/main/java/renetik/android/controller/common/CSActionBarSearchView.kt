package renetik.android.controller.common

import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import renetik.android.controller.R
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.base.findView
import renetik.android.framework.event.event
import renetik.android.framework.event.listen
import renetik.android.primitives.isSet
import renetik.android.view.imageView
import renetik.android.view.onClick

class CSActionBarSearchView(parent: CSActivityView<*>, viewId: Int, hint: String = "",
                            var text: String = "",
                            private val onChange: (String) -> Unit) : CSActivityView<SearchView>(parent,
    viewId), OnQueryTextListener, View.OnClickListener,
    SearchView.OnCloseListener {

    var eventOnClearButtonClick = event<CSActionBarSearchView>()
    private var searchOpened = false
    private var expanded = false
    private var hint: String? = hint
    private val clearButton by lazy { findView<ImageView>(R.id.search_close_btn)!! }

    override fun onViewReady() {
        super.onViewReady()
        view.onActionViewExpanded()
        view.setIconifiedByDefault(!expanded)
        view.isIconified = if (text.isSet) false else !expanded
        hint?.let { view.queryHint = it }
        if (!view.isFocused) view.clearFocus()
        if (text.isSet) view.setQuery(text, false)
        view.setOnQueryTextListener(this)
        view.setOnSearchClickListener(this)
        view.setOnCloseListener(this)
        clearButton.onClick {
            view.setQuery("", true)
            eventOnClearButtonClick.fire(this)
        }
        view.imageView(androidx.appcompat.R.id.search_mag_icon).onClick {
            view.requestFocus()
            showKeyboard()
        }
    }

    override fun onQueryTextSubmit(query: String?) = false

    override fun onQueryTextChange(newText: String): Boolean {
        text = newText
        onChange(newText)
        return false
    }

    override fun onClick(v: View?) {
        searchOpened = true
    }

    override fun onClose(): Boolean {
        searchOpened = false
        return false
    }

    fun clear() {
        view.setQuery("", false)
        view.clearFocus()
        view.isIconified = !expanded
    }

    fun expanded(value: Boolean) = apply { expanded = value }

    fun onClearButtonClick(listener: (CSActionBarSearchView) -> Unit) = apply {
        eventOnClearButtonClick.listen(listener)
    }

}


//        val colorOnPrimary = colorFromAttribute(R.attr.colorOnPrimary)
//        searchView.editText(androidx.appcompat.R.id.search_src_text).apply {
//            setTextColor(colorOnPrimary)
//            setLinkTextColor(colorOnPrimary.darken)
//            setHintTextColor(colorOnPrimary.darkenMore)
//        }
//        searchView.imageView(androidx.appcompat.R.id.search_mag_icon).iconTint(colorOnPrimary)
//        searchView.imageView(androidx.appcompat.R.id.search_go_btn).iconTint(colorOnPrimary)
//        searchView.imageView(androidx.appcompat.R.id.search_close_btn).iconTint(colorOnPrimary)
//        searchView.imageView(androidx.appcompat.R.id.search_voice_btn).iconTint(colorOnPrimary)
//        searchView.imageView(androidx.appcompat.R.id.search_button).iconTint(colorOnPrimary)
