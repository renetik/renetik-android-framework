package renetik.android.sample.view

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.view.View
import renetik.android.base.layout
import renetik.android.controller.base.CSViewController
import renetik.android.controller.common.CSLogDisplayController
import renetik.android.controller.common.CSNavigationItem
import renetik.android.controller.extensions.menuItem
import renetik.android.controller.extensions.requestPermissions
import renetik.android.dialog.extensions.dialog
import renetik.android.extensions.button
import renetik.android.java.collections.list
import renetik.android.sample.R
import renetik.android.sample.view.dynamicmenu.SampleDynamicMenuController
import renetik.android.sample.view.getpicture.SampleGetPictureController
import renetik.android.sample.view.list.SampleListController
import renetik.android.sample.view.maps.SampleMapController
import renetik.android.sample.view.request.SamplePagerController
import renetik.android.themes.CSThemeChooserController
import renetik.android.view.extensions.onClick
import renetik.android.view.extensions.title

class SampleMainController : CSViewController<View>(navigation, layout(R.layout.sample_main)),
        CSNavigationItem {

    init {
        menuItem("Theme chooser").onClick { CSThemeChooserController(navigation).push() }
        button(R.id.SampleMenu_ButtonList).onClick { SampleListController(it.title).push() }
        button(R.id.SampleMenu_ButtonMaps).onClick {
            requestPermissions(list(ACCESS_FINE_LOCATION),
                    onGranted = { SampleMapController(it.title).push() },
                    onNotGranted = { dialog("You need to accept permission request for maps to work") })
        }
        button(R.id.SampleMenu_ButtonGetPicture).onClick { SampleGetPictureController(it.title).push() }
        button(R.id.SampleMenu_ButtonDynamicMenu).onClick { SampleDynamicMenuController(it.title).push() }
        button(R.id.SampleMenu_ButtonRequest).onClick { SamplePagerController(it.title).push() }
        button(R.id.SampleMenu_ButtonLog).onClick { CSLogDisplayController(navigation, it.title).push() }
    }
}