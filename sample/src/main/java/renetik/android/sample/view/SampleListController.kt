package renetik.android.sample.view

import android.view.View
import android.widget.ListView
import renetik.android.extensions.view.*
import renetik.android.lang.CSLang.toast
import renetik.android.sample.R
import renetik.android.sample.model.SampleListItem
import renetik.android.sample.model.model
import renetik.android.view.list.CSListController
import renetik.android.view.list.CSRemoveListItemsController
import renetik.android.view.list.CSRowView
import renetik.android.viewbase.CSViewController

class SampleListController() : CSViewController<View>(navigation, layout(R.layout.sample_list)) {

    private var sampleList = CSListController<SampleListItem, ListView>(this, R.id.SampleList_List).apply {
        onCreateView { SampleListItemView(this) }
        onItemClick { _, rowView -> snackBarInfo("SampleListItemView clicked ${rowView.data().title}") }
        emptyView(R.id.SampleList_ListEmpty)
    }.reload(model.sampleList)

    init {
        CSRemoveListItemsController(this, sampleList, "Remove selected items ?") { toRemove ->
            toRemove.forEach { item -> model.sampleList.delete(item) }
            sampleList.reload(model.sampleList)
        }
        menu("Action").onClick { snackBarInfo("This is some action") }.alwaysAsAction()
        menu("Menu item 1").onClick { toast("This is item 1 click") }.neverAsAction()
        menu("Menu item 2").neverAsAction().onClick {
            dialog("This is item 2 click").show { dialog("and dialog button click").show() }
        }
        button(R.id.SampleList_BottomButton).onClick { dialog("Bottom button click").show() }
    }
}

class SampleListItemView(list: CSListController<SampleListItem, ListView>) :
        CSRowView<SampleListItem>(list, layout(R.layout.sample_list_item)) {
    override fun onLoad(item: SampleListItem) {
        textView(R.id.header).title(item.time)
        textView(R.id.title).title(item.title)
        textView(R.id.subtitle).title(item.subtitle)
    }
}
