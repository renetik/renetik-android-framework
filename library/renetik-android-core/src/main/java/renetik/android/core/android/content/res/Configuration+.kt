package renetik.android.core.android.content.res

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES

val Configuration.uiModeNight get() = uiMode and UI_MODE_NIGHT_MASK
val Configuration.isDarkMode get() = uiModeNight == UI_MODE_NIGHT_YES
val Configuration.isLightMode get() = uiModeNight == UI_MODE_NIGHT_NO
