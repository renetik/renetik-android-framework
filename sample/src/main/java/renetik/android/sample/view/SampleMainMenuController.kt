package renetik.android.sample.view

import android.graphics.Color
import android.view.View
import renetik.android.extensions.view.*
import renetik.android.sample.R
import renetik.android.view.base.CSViewController

class SampleMainMenuController : CSViewController<View>(navigation) {

    override fun createView() = VerticalLayout(R.style.CSContainer) {
        Button(layoutMatchFill, "Example List", R.style.CSTextHeadline6) {
            backgroundTint(Color.YELLOW)
            textColor(R.color.cs_white)
            onClick { SampleListController().push() }
        }
        Button(layoutMatchFill, "Example List2", R.style.CSTextHeadline6) {
            backgroundTint(Color.RED)
            textColor(R.color.cs_white)
            onClick { SampleListController().push() }
        }
    }
}


