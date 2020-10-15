package renetik.android.controller.common

import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import renetik.android.controller.R
import renetik.android.controller.base.CSViewController
import renetik.android.controller.extensions.findView
import renetik.android.java.event.event
import renetik.android.java.event.listen
import renetik.android.java.extensions.isSet
import renetik.android.view.extensions.imageView
import renetik.android.view.extensions.onClick

class CSSearchController : CSViewController<SearchView>, OnQueryTextListener, View.OnClickListener,
    SearchView.OnCloseListener {

    var text = ""
    var eventOnClearButtonClick = event<CSSearchController>()
    private var searchOpened = false
    private var expanded = false
    private var hint: String? = null
    private val clearButton by lazy { findView<ImageView>(R.id.search_close_btn)!! }
    private val listener: (String) -> Unit

    constructor(
        parent: CSViewController<View>, hint: String = "", text: String = "",
        listener: (String) -> Unit
    ) : super(parent) {
        this.hint = hint
        this.text = text
        this.listener = listener
        setView(SearchView(this))
    }

    constructor(
        parent: CSViewController<*>, search: SearchView, hint: String = "", text: String = "",
        onChange: (String) -> Unit
    ) : super(parent) {
        this.hint = hint
        this.text = text
        this.listener = onChange
        setView(search)
    }

    constructor(
        parent: CSViewController<*>, viewId: Int, hint: String = "", text: String = "",
        listener: (String) -> Unit
    ) : super(parent, viewId) {
        this.hint = hint
        this.text = text
        this.listener = listener
    }

    override fun onViewShowingFirstTime() {
        super.onViewShowingFirstTime()
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
        listener(newText)
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

    fun onClearButtonClick(listener: (CSSearchController) -> Unit) = apply {
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
