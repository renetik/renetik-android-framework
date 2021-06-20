package renetik.android.sample.view

import renetik.android.controller.base.CSActivity
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.common.CSNavigationView
import renetik.android.dialog.CSDialog
import renetik.android.sample.R
import renetik.android.themes.CSThemes

lateinit var navigation: CSNavigationView

fun CSActivityView<*>.push() = navigation.push(this)

class SampleNavigation(activity: CSActivity) : CSNavigationView(activity) {
    override fun onViewShowingFirstTime() {
        navigation = this
        push(SampleMainView())
    }
}

class SampleNavigationActivity : CSActivity() {
    override fun createController(): CSNavigationView {
        CSThemes.initializeThemes(this)
        CSDialog.defaults { withIcon(R.drawable.om_black_196) }
        return SampleNavigation(this)
    }
}

