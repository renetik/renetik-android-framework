@file:Suppress("DEPRECATION")

package renetik.android.ui.extensions

import android.app.Activity
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU

fun Activity.disablePendingAnimations() {
    if (SDK_INT >= TIRAMISU) overridePendingTransition(0, 0, 0)
    else overridePendingTransition(0, 0)
}