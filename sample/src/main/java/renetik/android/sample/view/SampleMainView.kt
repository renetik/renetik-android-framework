package renetik.android.sample.view

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.view.View
import renetik.android.framework.lang.CSLayoutRes.Companion.layout
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.common.CSLogDisplayView
import renetik.android.controller.common.CSNavigationItem
import renetik.android.controller.extensions.button
import renetik.android.controller.extensions.dialog
import renetik.android.controller.extensions.menuItem
import renetik.android.controller.extensions.requestPermissions
import renetik.android.java.extensions.collections.list
import renetik.android.sample.R
import renetik.android.sample.view.dynamicmenu.SampleDynamicMenuView
import renetik.android.sample.view.getpicture.SampleGetPictureView
import renetik.android.sample.view.list.SampleListView
import renetik.android.sample.view.maps.SampleMapView
import renetik.android.sample.view.request.SamplePagerView
import renetik.android.themes.CSThemeChooserView
import renetik.android.view.extensions.title

class SampleMainView : CSActivityView<View>(navigation, layout(R.layout.sample_main)),
    CSNavigationItem {

    init {
        menuItem("Theme chooser") { CSThemeChooserView(navigation).push() }
        button(R.id.SampleMenu_ButtonList) { SampleListView(it.title).push() }
        button(R.id.SampleMenu_ButtonMaps) {
            requestPermissions(list(ACCESS_FINE_LOCATION),
                               onGranted = { SampleMapView(it.title).push() },
                               onNotGranted = { dialog("You need to accept permission request for maps to work") })
        }
        button(R.id.SampleMenu_ButtonGetPicture) { SampleGetPictureView(it.title).push() }
        button(R.id.SampleMenu_ButtonDynamicMenu) { SampleDynamicMenuView(it.title).push() }
        button(R.id.SampleMenu_ButtonRequest) { SamplePagerView(it.title).push() }
        button(R.id.SampleMenu_ButtonLog) {
            CSLogDisplayView(navigation, it.title).push()
        }
    }
}