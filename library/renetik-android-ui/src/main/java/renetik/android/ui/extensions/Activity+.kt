@file:Suppress("DEPRECATION")

package renetik.android.ui.extensions

import android.app.Activity
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import renetik.android.ui.R

fun Activity.disablePendingAnimations() {
    if (SDK_INT >= TIRAMISU) overridePendingTransition(R.anim.cs_anim_none, R.anim.cs_anim_none, 0)
    else overridePendingTransition(R.anim.cs_anim_none, R.anim.cs_anim_none)
}