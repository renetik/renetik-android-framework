package renetik.android.core.android.content

import android.content.Context
import android.content.res.Configuration.ORIENTATION_PORTRAIT

val Context.isPortrait get() = resources.configuration.orientation == ORIENTATION_PORTRAIT
val Context.isLandscape get() = !isPortrait
val Context.isTablet get() = resources.configuration.smallestScreenWidthDp >= 600
val Context.isPhone get() = !isTablet
val Context.isScreenWide get() = isTablet || isLandscape
