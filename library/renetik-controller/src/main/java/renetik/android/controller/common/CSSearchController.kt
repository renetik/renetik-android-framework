package renetik.android.controller.common

import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import renetik.android.controller.base.CSViewController


class CSSearchController : CSViewController<SearchView> {

    var text = ""
    private var searchListener: ((String) -> Unit)?
    private var searchOpened = false
    private var expanded = false
    private lateinit var searchView: SearchView
    private var hint: String? = null

    override fun obtainView() = searchView

    constructor(parent: CSViewController<*>,
                hint: String = "Enter search text",
                searchListener: (String) -> Unit)
            : super(parent) {
        this.hint = hint
        searchView = SearchView(this)
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
        this.searchListener = searchListener
    }

    constructor(parent: CSViewController<*>, search: SearchView, searchListener: (String) -> Unit)
            : super(parent) {
        searchView = search
        this.searchListener = searchListener
    }

    constructor(parent: CSViewController<*>, viewId: Int, searchListener: (String) -> Unit)
            : super(parent, viewId) {
        this.searchListener = searchListener
    }

    override fun onViewShowingFirstTime() {
        super.onViewShowingFirstTime()
        initializeSearch()
    }

    private fun initializeSearch() {
        searchView.onActionViewExpanded()
        searchView.setIconifiedByDefault(!expanded)
        view.isIconified = !expanded
        hint?.let { searchView.queryHint = it }
        if (!searchView.isFocused) searchView.clearFocus()

        view.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String) = false
            override fun onQueryTextChange(value: String): Boolean {
                text = value
                searchListener?.invoke(value)
                return false
            }
        })
        view.setOnSearchClickListener { searchOpened = true }
        view.setOnCloseListener {
            searchOpened = false
            false
        }
    }

    fun clear() {
        val tmpListener = searchListener
        searchListener = null
        view.setQuery("", false)
        view.clearFocus()
        view.isIconified = true
        searchListener = tmpListener
    }

    fun expanded(value: Boolean) = apply { expanded = value }
}
