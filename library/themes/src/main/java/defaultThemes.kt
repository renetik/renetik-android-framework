package renetik.android.themes

import renetik.android.java.collections.list

val defaultThemes
    get() = list(
            Theme("Theme BlueGray Green", R.style.CSThemeBlueGrayGreen),
            Theme("Theme Cyan", R.style.CSThemeCyan),
            Theme("Theme Teal Yellow", R.style.CSThemeTealYellow),
            Theme("Theme Cyan Teal", R.style.CSThemeCyanTeal),
            Theme("Theme Purple", R.style.CSThemePurple),
            Theme("Theme Pink", R.style.CSThemePink),
            Theme("Theme Teal Red", R.style.CSThemeTealRed)
    )