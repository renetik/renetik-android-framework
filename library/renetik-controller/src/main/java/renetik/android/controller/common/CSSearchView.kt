package renetik.android.controller.common

import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnCloseListener
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import renetik.android.content.attributeColor
import renetik.android.content.color
import renetik.android.controller.R
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.base.editText
import renetik.android.controller.base.findView
import renetik.android.framework.event.event
import renetik.android.framework.event.listen
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.primitives.isSet
import renetik.android.view.extensions.visibleIf
import renetik.android.view.imageView
import renetik.android.view.onClick

// TODO move to standard text view ..
//  it makes not sense to use SearchView at all if not in action bar
class CSSearchView(
    parent: CSActivityView<*>,
    viewId: Int,
    hint: String = "",
    var text: String = "",
    private val onChange: (String) -> Unit) : CSActivityView<SearchView>(parent, viewId),
    OnQueryTextListener, OnClickListener, OnCloseListener {

    constructor(parent: CSActivityView<*>, viewId: Int,
                property: CSEventProperty<String>, hint: String = "")
            : this(parent, viewId, hint, property.value, { property.value = it })

    var eventOnClearButtonClick = event<CSSearchView>()
    private var searchOpened = false
    private var hint: String? = hint
    private val clearButton by lazy { findView<ImageView>(R.id.search_close_btn)!! }

    override fun onViewReady() {
        super.onViewReady()
        hint?.let { view.queryHint = it }
        view.isIconified = false
        if (!view.isFocused) view.clearFocus()
        if (text.isSet) view.setQuery(text, false)
        updateClearButton()
        view.setOnQueryTextListener(this)
        view.setOnSearchClickListener(this)
        view.setOnCloseListener(this)
        clearButton.onClick {
            view.setQuery("", true)
            eventOnClearButtonClick.fire(this)
            updateClearButton()
        }
        view.imageView(androidx.appcompat.R.id.search_mag_icon).onClick {
            view.requestFocus()
            showKeyboard()
        }
        findView<View>(R.id.search_plate)?.setBackgroundColor(color(R.color.cs_transparent).color)
        editText(androidx.appcompat.R.id.search_src_text).apply {
//            setTextColor(attributeColor(R.attr.colorPrimary))
//            setLinkTextColor(attributeColor(R.attr.colorPrimary))
//            setHintTextColor(attributeColor(R.attr.colorPrimaryVariant))
        }
    }

    override fun onQueryTextSubmit(query: String?) = false

    override fun onQueryTextChange(newText: String): Boolean {
        text = newText
        updateClearButton()
        onChange(newText)
        return false
    }

    private fun updateClearButton() {
        clearButton.visibleIf(text.isSet)
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
        updateClearButton()
    }

    fun onClearButtonClick(listener: (CSSearchView) -> Unit) = apply {
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
