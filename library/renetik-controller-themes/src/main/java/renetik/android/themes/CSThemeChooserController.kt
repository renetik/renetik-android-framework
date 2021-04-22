package renetik.android.themes

import android.view.View
import android.widget.GridView
import renetik.android.base.CSLayoutRes.Companion.layout
import renetik.android.controller.base.CSViewController
import renetik.android.controller.common.CSNavigationController
import renetik.android.controller.common.CSNavigationItem
import renetik.android.controller.extensions.dialog
import renetik.android.controller.extensions.snackBarInfo
import renetik.android.controller.extensions.textView
import renetik.android.controller.extensions.view
import renetik.android.listview.CSListController
import renetik.android.listview.CSRowView
import renetik.android.themes.CSThemes.Companion.applyTheme
import renetik.android.themes.CSThemes.Companion.availableThemes
import renetik.android.themes.CSThemes.Companion.currentTheme
import renetik.android.themes.CSThemes.Companion.currentThemeIndex
import renetik.android.view.extensions.text

class CSThemeChooserController(val navigation: CSNavigationController,
                               title: String = "Theme Chooser")
    : CSViewController<View>(navigation, layout(R.layout.theme_chooser)), CSNavigationItem {

    init {
        textView(R.id.ThemeSwitcher_Title).text(currentTheme.title)
        CSListController<CSTheme, GridView>(this, R.id.ThemeSwitcher_Grid) {
            CSRowView(this, layout(R.layout.theme_switcher_item), onLoadSwitchItem)
        }.onItemClick(R.id.ThemeSwitcherItem_Button) { row ->
            if (row.index == currentThemeIndex) snackBarInfo("Current theme...")
            else dialog("You selected theme: ${row.row.title}")
                .show("Apply selected theme ?") { applyTheme(activity(), row.index) }
        }.reload(availableThemes).apply { currentThemeIndex?.let { selectedIndex(it) } }
    }

    private val onLoadSwitchItem: (CSRowView<CSTheme>.(CSTheme) -> Unit) = { theme ->
        val attributes = obtainStyledAttributes(theme.style, R.styleable.Theme)
        view(R.id.ThemeSwitcherItem_ColorPrimary)
            .setBackgroundColor(attributes.getColor(R.styleable.Theme_colorPrimary, 0))
        view(R.id.ThemeSwitcherItem_ColorPrimaryVariant)
            .setBackgroundColor(attributes.getColor(R.styleable.Theme_colorPrimaryVariant, 0))
        view(R.id.ThemeSwitcherItem_ColorSecondary)
            .setBackgroundColor(attributes.getColor(R.styleable.Theme_colorSecondary, 0))
        view(R.id.ThemeSwitcherItem_ColorSecondaryVariant)
            .setBackgroundColor(attributes.getColor(R.styleable.Theme_colorSecondaryVariant, 0))
        view(R.id.ThemeSwitcherItem_ColorOnPrimary)
            .setBackgroundColor(attributes.getColor(R.styleable.Theme_colorOnPrimary, 0))
        view(R.id.ThemeSwitcherItem_ColorOnSecondary)
            .setBackgroundColor(attributes.getColor(R.styleable.Theme_colorOnSecondary, 0))
        attributes.recycle()
        textView(R.id.ThemeSwitcherItem_Title).text(theme.title)
    }

    override val navigationItemTitle = title
}