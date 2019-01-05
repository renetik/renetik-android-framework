package renetik.android.sample.view.request

import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import renetik.android.base.layout
import renetik.android.controller.base.CSViewController
import renetik.android.controller.common.CSSearchController
import renetik.android.controller.pager.CSPagerPage
import renetik.android.dialog.extensions.dialog
import renetik.android.framework.extensions.sendWithFailedDialog
import renetik.android.framework.extensions.sendWithProgress
import renetik.android.imaging.extensions.image
import renetik.android.listview.CSListController
import renetik.android.listview.CSListLoadNextController
import renetik.android.listview.CSRemoveListRowsController
import renetik.android.listview.CSRowView
import renetik.android.sample.R
import renetik.android.sample.model.ListItemData
import renetik.android.sample.model.model
import renetik.android.view.extensions.imageView
import renetik.android.view.extensions.textView
import renetik.android.view.extensions.title

class SamplePageListController(parent: CSViewController<ViewGroup>, title: String)
    : CSViewController<View>(parent, layout(R.layout.sample_page_list)), CSPagerPage {

    private val listController = CSListController<ListItemData, ListView>(this, R.id.SamplePageList_List) {
        CSRowView(this, layout(R.layout.sample_page_list_item)) { row -> view.loadPageListItem(row) }
    }.onItemClick { rowView ->
        dialog().showView(inflate<View>(R.layout.sample_page_list_item).loadPageListItem(rowView.data))
    }.emptyView(R.id.SamplePageList_ListEmpty)

    private val search = CSSearchController(this) { reloadList() }

    init {
        CSListLoadNextController(listController, R.layout.cs_list_load_next) {
            loadNextListPage(it.pageNumber)
        }
        CSRemoveListRowsController(listController, "Remove selected items ?") { toRemove ->
            model.server.deleteSampleListItems(toRemove)
                    .sendWithProgress("Deleting list item").onSuccess { reloadList() }
        }
    }

    override fun onViewShowingFirstTime() {
        super.onViewShowingFirstTime()
        reloadList()
    }

    private fun View.loadPageListItem(row: ListItemData) = apply {
        imageView(R.id.SamplePageListItem_Image).image(row.image)
        textView(R.id.SamplePageListItem_Title).title(row.name)
        textView(R.id.SamplePageListItem_Subtitle).title(row.description)
    }

    private fun reloadList() {
        model.server.loadSampleList(1, search.text).sendWithProgress("Loading list items")
                .onSuccess { listController.reload(it.list) }
    }

    private fun loadNextListPage(page: Int) {
        model.server.loadSampleList(page, search.text).sendWithFailedDialog("Loading list items")
                .onSuccess { listController.loadAdd(it.list) }
    }

    override val pagerPageTitle = title
}