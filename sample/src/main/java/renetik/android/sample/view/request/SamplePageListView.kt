package renetik.android.sample.view.request

import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import renetik.android.framework.lang.CSLayoutRes.Companion.layout
import renetik.android.client.okhttp3.extensions.image
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.extensions.dialog
import renetik.android.controller.extensions.swipeRefresh
import renetik.android.controller.pager.CSPagerPage
import renetik.android.dialog.showView
import renetik.android.framework.extensions.send
import renetik.android.listview.CSRowView
import renetik.android.listview.actions.CSRemoveListRowsView
import renetik.android.listview.extensions.listController
import renetik.android.listview.request.CSRequestListView
import renetik.android.listview.request.CSRequestListLoadNextView
import renetik.android.sample.R
import renetik.android.sample.model.ServerListItem
import renetik.android.sample.model.model
import renetik.android.view.extensions.imageView
import renetik.android.view.extensions.text
import renetik.android.view.extensions.textView

class SamplePageListView(parent: CSActivityView<ViewGroup>, title: String)
    : CSActivityView<View>(parent, layout(R.layout.sample_page_list)), CSPagerPage {

    val listController =
        CSRequestListView<ServerListItem, ListView>(this, R.id.SamplePageList_List) {
            CSRowView(this, layout(R.layout.sample_page_list_item)) { row ->
                view.loadPageListItem(row)
            }
        }

    init {
        listController.onReload { progress ->
            model.server.loadSampleList(page = 1)
                .send(getString(R.string.SampleDynamicMenu_Text), progress)
        }.onItemClick { view ->
            dialog("List item:").showView(R.layout.sample_page_list_item)
                .loadPageListItem(view.row)
        }.emptyView(R.id.SamplePageList_ListEmpty)

        CSRequestListLoadNextView(listController, R.layout.cs_list_load_next) {
            model.server.loadSampleList(it.pageNumber).send("Loading list items", isProgress = false)
        }
        CSRemoveListRowsView(listController, "Remove selected items ?") { toRemove ->
            model.server.deleteSampleListItems(toRemove).send("Deleting list item")
                .onSuccess { listController.reload(progress = true).refresh() }
        }
        swipeRefresh(R.id.SamplePageList_Pull).listController(listController)
    }

    override fun onViewShowingFirstTime() {
        super.onViewShowingFirstTime()
        listController.reload(progress = true).refresh()
    }

    private fun View.loadPageListItem(row: ServerListItem) = apply {
        imageView(R.id.SamplePageListItem_Image).image(row.image)
        textView(R.id.SamplePageListItem_Title).text(row.name)
        textView(R.id.SamplePageListItem_Subtitle).text(row.description)
    }

    override val pagerPageTitle = title
}
