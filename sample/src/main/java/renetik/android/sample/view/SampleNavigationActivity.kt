package renetik.android.sample.view

import renetik.android.controller.base.CSActivity
import renetik.android.controller.base.CSViewController
import renetik.android.controller.common.CSNavigationController
import renetik.android.themes.defaultThemes
import renetik.android.themes.extensions.initializeThemes

lateinit var navigation: CSNavigationController
fun CSViewController<*>.push() = navigation.push(this)

class SampleNavigationActivity : CSActivity() {
    override fun createController(): CSNavigationController {
        initializeThemes(defaultThemes)
        return object : CSNavigationController(this) {
            override fun onViewShowingFirstTime() {
                navigation = this
                SampleMainMenuController().push()
            }
        }
    }
}

