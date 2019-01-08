package renetik.android.themes

import renetik.android.java.collections.list

var currentThemeIndex: Int? = null
var availableThemes = list<Theme>()

class Theme(val title: String, val style: Int)

val defaultThemes
    get() = list(
            Theme("Theme Purple 2", R.style.CSThemePurple2),
            Theme("Theme Cyan Teal", R.style.CSThemeCyanTeal),
            Theme("Theme Cyan 2", R.style.CSThemeCyan2),
            Theme("Theme Purple", R.style.CSThemePurple),
            Theme("Theme Teal Red", R.style.CSThemeTealRed),
            Theme("Theme Teal Yellow", R.style.CSThemeTealYellow),
            Theme("Theme Cyan", R.style.CSThemeCyan),
            Theme("Theme Pink", R.style.CSThemePink),
            Theme("Theme BlueGray Green", R.style.CSThemeBlueGrayGreen),
            Theme("Theme Red", R.style.CSThemeRed)
    )