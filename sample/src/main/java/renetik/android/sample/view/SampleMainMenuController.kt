package renetik.android.sample.view

import android.view.View
import org.jetbrains.anko.sdk21.coroutines.onClick
import renetik.android.base.layout
import renetik.android.controller.base.CSViewController
import renetik.android.controller.common.CSLogController
import renetik.android.extensions.button
import renetik.android.sample.R
import renetik.android.themes.CSThemeSwitcherController

class SampleMainMenuController : CSViewController<View>(navigation, layout(R.layout.sample_menu)) {
    override fun onViewShowingFirstTime() {
        button(R.id.SampleMenu_ButtonThemes).onClick { CSThemeSwitcherController(navigation).push() }
        button(R.id.SampleMenu_ButtonLog).onClick { CSLogController(navigation).push() }
        button(R.id.SampleMenu_ButtonList).onClick { SampleListController().push() }
    }
}


