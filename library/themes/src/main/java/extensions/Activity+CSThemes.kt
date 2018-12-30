package renetik.android.themes.extensions

import android.app.Activity
import renetik.android.java.extensions.collections.reload
import renetik.android.themes.Theme
import renetik.android.themes.availableThemes
import renetik.android.themes.currentThemeIndex

fun Activity.initializeThemes(themes: List<Theme>) {
    availableThemes.reload(themes)
    currentThemeIndex = renetik.android.base.application.store.loadInt("theme_index", 0)
            .also { setTheme(availableThemes[it].style) }
}