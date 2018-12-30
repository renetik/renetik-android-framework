package renetik.android.sample.view

import android.view.View
import android.widget.ListView
import renetik.android.base.layout
import renetik.android.controller.base.CSViewController
import renetik.android.controller.common.CSNavigationItem
import renetik.android.controller.common.CSSearchController
import renetik.android.dialog.extensions.dialog
import renetik.android.extensions.button
import renetik.android.extensions.textView
import renetik.android.extensions.title
import renetik.android.java.collections.list
import renetik.android.java.extensions.collections.delete
import renetik.android.listview.CSListController
import renetik.android.listview.CSRemoveListRowsController
import renetik.android.listview.CSRowView
import renetik.android.logging.CSLog.logInfoToast
import renetik.android.material.extensions.snackBarInfo
import renetik.android.sample.R
import renetik.android.sample.model.SampleListRow
import renetik.android.sample.model.model
import renetik.android.view.extensions.onClick
import renetik.android.view.extensions.title

class SampleListController(private val title: String)
    : CSViewController<View>(navigation, layout(R.layout.sample_list)), CSNavigationItem {

    private val listController = CSListController<SampleListRow, ListView>(this, R.id.SampleList_List) {
        CSRowView(this, layout(R.layout.sample_list_item)) { row ->
            textView(R.id.header).title(row.time)
            textView(R.id.title).title(row.title)
            textView(R.id.subtitle).title(row.subtitle)
        }
    }.onItemClick { rowView -> snackBarInfo("SampleListItemView clicked ${rowView.data.title}") }
            .emptyView(R.id.SampleList_ListEmpty)
    private val searchController = CSSearchController(this) { reloadList() }

    init {
        title(R.id.SampleList_Title, title)
        CSRemoveListRowsController(listController, "Remove selected items ?") { toRemove ->
            toRemove.forEach { item -> model.sampleList.delete(item) }
            listController.reload(model.sampleList)
        }
        menuItem(searchController.view)
        menuItem("Action").onClick { snackBarInfo("This is some action") }.alwaysAsAction()
        menuItem("Menu item 1").onClick { logInfoToast("This is item 1 click") }.neverAsAction()
        menuItem("Menu item 2").neverAsAction().onClick {
            dialog("This is item 2 click").show { dialog("and dialog Button click").show() }
        }
        button(R.id.SampleList_BottomButton).onClick { dialog("Bottom Button click").show() }
        reloadList()
    }

    private fun reloadList() {
        val listData =
                if (searchController.text.isEmpty()) model.sampleList
                else list<SampleListRow>().apply {
                    for (row in model.sampleList)
                        if (row.searchableText.contains(searchController.text, ignoreCase = true))
                            add(row)
                }
        listController.reload(listData)
    }

    override val navigationItemTitle = ""
}