package renetik.android.view

import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import renetik.android.view.base.CSViewController
import renetik.android.view.menu.CSMenuItem
import renetik.java.lang.CSLang

class CSSearchController : CSViewController<SearchView> {

    private var query = ""
    private var queryListener: ((String) -> Unit)? = null
    fun queryListener(function: (String) -> Unit) = apply { queryListener = function }
    private var searchOpened = false
    private var expanded = false
    fun expanded(value: Boolean) = apply { expanded = value }
    private var searchMenuItem: CSMenuItem? = null

    override fun createView() = searchMenuItem?.actionView as? SearchView

    constructor(parent: CSViewController<*>, menuItem: CSMenuItem) : super(parent) {
        searchMenuItem = menuItem
    }

    constructor(parent: CSViewController<*>, viewId: Int) : super(parent, viewId)

    override fun onViewShowingFirstTime() {
        super.onViewShowingFirstTime()
        initializeSearch()
    }

    private fun initializeSearch() {
        view.setQuery(query, CSLang.NO)
        view.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String) = CSLang.NO

            override fun onQueryTextChange(value: String): Boolean {
                query = value
                queryListener?.invoke(value)
                return false
            }
        })
        view.setOnSearchClickListener { searchOpened = CSLang.YES }
        view.setOnCloseListener {
            searchOpened = CSLang.NO
            false
        }
        view.setIconifiedByDefault(!expanded)
        view.isIconified = !expanded
        view.requestFocusFromTouch()
    }

    fun clear() {
        val tmpListener = queryListener
        queryListener = null
        view.setQuery("", CSLang.NO)
        view.clearFocus()
        view.isIconified = true
        queryListener = tmpListener
    }
}
