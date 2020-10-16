package renetik.android.extensions

import android.app.Activity
import android.view.View

val Activity.contentView1
    get() = window.findViewById<View>(android.R.id.content)

val Activity.contentView2
    get() = window.decorView.rootView