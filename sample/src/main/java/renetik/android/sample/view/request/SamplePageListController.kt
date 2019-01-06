package renetik.android.sample.view.request

import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import renetik.android.base.layout
import renetik.android.controller.base.CSViewController
import renetik.android.controller.common.CSSearchController
import renetik.android.controller.pager.CSPagerPage
import renetik.android.dialog.extensions.dialog
import renetik.android.dialog.showView
import renetik.android.extensions.swipeRefresh
import renetik.android.framework.extensions.send
import renetik.android.framework.extensions.sendWithProgress
import renetik.android.imaging.extensions.image
import renetik.android.listview.CSListController
import renetik.android.listview.CSListLoadNextController
import renetik.android.listview.CSRemoveListRowsController
import renetik.android.listview.CSRowView
import renetik.android.sample.R
import renetik.android.sample.model.SampleListItem
import renetik.android.sample.model.model
import renetik.android.view.extensions.*

class SamplePageListController(parent: CSViewController<ViewGroup>, title: String)
    : CSViewController<View>(parent, layout(R.layout.sample_page_list)), CSPagerPage {

    private val listController = CSListController<SampleListItem, ListView>(this, R.id.SamplePageList_List) {
        CSRowView(this, layout(R.layout.sample_page_list_item)) { row -> view.loadPageListItem(row) }
    }.onItemClick { rowView ->
        dialog().showView(R.layout.sample_page_list_item).loadPageListItem(rowView.data)
    }.emptyView(R.id.SamplePageList_ListEmpty)

    private val search: CSSearchController = CSSearchController(this) { listReload(progress = true) }

    init {
        CSListLoadNextController(listController,
                R.layout.cs_list_load_next) { listLoadNext(it.pageNumber) }
        CSRemoveListRowsController(listController, "Remove selected items ?") { toRemove ->
            model.server.deleteSampleListItems(toRemove)
                    .sendWithProgress("Deleting list item").onSuccess { listReload(progress = true) }
        }
        swipeRefresh(R.id.SamplePageList_Pull)
                .onRefresh { pull -> listReload(false).onDone { pull.onDone() } }
    }

    override fun onViewShowingFirstTime() {
        super.onViewShowingFirstTime()
        listReload(progress = true)
    }

    private fun View.loadPageListItem(row: SampleListItem) = apply {
        imageView(R.id.SamplePageListItem_Image).image(row.image)
        textView(R.id.SamplePageListItem_Title).title(row.name)
        textView(R.id.SamplePageListItem_Subtitle).title(row.description)
    }

    private fun listReload(progress: Boolean) = model.server.loadSampleList(1, search.text)
            .send("Loading list items", progress).onSuccess { listController.reload(it.list) }

    private fun listLoadNext(page: Int) = model.server.loadSampleList(page, search.text)
            .send("Loading list items", withProgress = false).onSuccess { listController.load(it.list) }

    override val pagerPageTitle = title
}
