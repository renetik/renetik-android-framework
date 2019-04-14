package renetik.android.themes

import android.app.Activity
import renetik.android.base.application
import renetik.android.java.extensions.collections.list
import renetik.android.java.extensions.collections.at
import renetik.android.java.extensions.collections.reload

class CSTheme(val title: String, val style: Int)

class CSThemes {
    companion object {
        var currentThemeIndex: Int? = null
        val availableThemes = default

        fun theme(index: Int) = availableThemes.at(index) ?: availableThemes[0]
        val currentTheme get() = theme(currentThemeIndex!!)

        fun initializeThemes(activity: Activity, themes: List<CSTheme>? = null) {
            themes?.let { availableThemes.reload(themes) }
            currentThemeIndex = application.store.loadInt("theme_index", 0)
            activity.setTheme(currentTheme.style)
        }

        fun applyTheme(activity: Activity, themeIndex: Int) {
            application.store.save("theme_index", themeIndex)
            activity.recreate()
        }

        val default
            get() = list(
                    CSTheme("Theme Purple 2", R.style.CSThemePurple2),
                    CSTheme("Theme Cyan Teal", R.style.CSThemeCyanTeal),
                    CSTheme("Theme Cyan 2", R.style.CSThemeCyan2),
                    CSTheme("Theme Purple", R.style.CSThemePurple),
                    CSTheme("Theme Teal Red", R.style.CSThemeTealRed),
                    CSTheme("Theme Teal Yellow", R.style.CSThemeTealYellow),
                    CSTheme("Theme Cyan", R.style.CSThemeCyan),
                    CSTheme("Theme Pink", R.style.CSThemePink),
                    CSTheme("Theme BlueGray Green", R.style.CSThemeBlueGrayGreen),
                    CSTheme("Theme Red", R.style.CSThemeRed)
            )
    }
}
