package renetik.android.view

import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import renetik.android.extensions.NO
import renetik.android.extensions.YES
import renetik.android.extensions.notNull
import renetik.android.viewbase.CSViewController
import renetik.android.viewbase.menu.CSMenuItem

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
        view.setQuery(query, NO)
        view.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String) = NO

            override fun onQueryTextChange(value: String): Boolean {
                query = value
                queryListener?.invoke(value)
                return false
            }
        })
        view.setOnSearchClickListener { searchOpened = YES }
        view.setOnCloseListener {
            searchOpened = NO
            false
        }
        view.setIconifiedByDefault(!expanded)
        view.isIconified = !expanded
        view.requestFocusFromTouch()
    }

    fun clear() {
        val tmpListener = queryListener
        queryListener = null
        view.setQuery("", NO)
        view.clearFocus()
        view.isIconified = true
        queryListener = tmpListener
    }
}
