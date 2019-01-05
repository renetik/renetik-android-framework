package renetik.android.sample.view.request

import android.view.ViewGroup
import renetik.android.base.layout
import renetik.android.controller.base.CSViewController
import renetik.android.controller.pager.CSPagerController
import renetik.android.extensions.textView
import renetik.android.java.collections.list
import renetik.android.material.extensions.floatingButton
import renetik.android.sample.R
import renetik.android.sample.view.navigation
import renetik.android.view.extensions.onClick
import renetik.android.view.extensions.title

class SamplePagerController(val title: String) : CSViewController<ViewGroup>(navigation, layout(R.layout.sample_pager)) {
    init {
        textView(R.id.SamplePager_Title).title(title)
        CSPagerController(this, R.id.SamplePager_Pager, list(
                SamplePageListController(this, "First"), SamplePageListController(this, "Second"),
                SamplePageListController(this, "Third"), SamplePageListController(this, "Fourth"),
                SamplePageListController(this, "Fifth"), SamplePageListController(this, "Sixth"),
                SamplePageListController(this, "Seventh"), SamplePageListController(this, "Eight")))
        floatingButton(R.id.SamplePager_ButtonAdd).onClick {

        }
    }
}