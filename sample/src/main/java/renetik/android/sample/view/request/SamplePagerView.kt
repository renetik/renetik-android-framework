package renetik.android.sample.view.request

import android.view.ViewGroup
import renetik.android.framework.lang.CSLayoutRes.Companion.layout
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.extensions.dialog
import renetik.android.controller.pager.CSPagerView
import renetik.android.controller.extensions.textView
import renetik.android.java.extensions.collections.list
import renetik.android.controller.extensions.floatingButton
import renetik.android.dialog.showView
import renetik.android.sample.R
import renetik.android.sample.view.navigation
import renetik.android.view.extensions.editText
import renetik.android.view.extensions.onClick
import renetik.android.view.extensions.text

class SamplePagerView(val title: String)
    : CSActivityView<ViewGroup>(navigation, layout(R.layout.sample_pager)) {

    private val pager = CSPagerView<SamplePageListView>(this, R.id.SamplePager_Pager)

    init {
        textView(R.id.SamplePager_Title).text(title)
        floatingButton(R.id.SamplePager_ButtonAdd).onClick { showAddItemDialog() }
        reloadPager()
    }

    private fun showAddItemDialog() = dialog("Add item to list").showView(R.layout.sample_pager_add_item) { onAction ->
        val nameView = onAction.view.editText(R.id.SamplePagerAddItem_Name)
        val descView = onAction.view.editText(R.id.SamplePagerAddItem_Description)
        true // TODO broken
//        list(nameView, descView).validateNotEmpty("Cannot be empty") {
//            model.server.addSampleListItem(ServerListItem().apply {
//                name.string = nameView.title
//                description.string = descView.title
//            }).send("Posting item to server", progress = true)
//                    .onSuccess { pager.current!!.listController.prependData(it.value) }
//        }
    }

    private fun reloadPager() = pager.reload(list(
        SamplePageListView(this, "First"), SamplePageListView(this, "Second"),
        SamplePageListView(this, "Third"), SamplePageListView(this, "Fourth"),
        SamplePageListView(this, "Fifth"), SamplePageListView(this, "Sixth"),
        SamplePageListView(this, "Seventh"), SamplePageListView(this, "Eight")))
}

