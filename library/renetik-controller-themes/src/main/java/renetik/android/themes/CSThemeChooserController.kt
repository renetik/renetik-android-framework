package renetik.android.themes

import android.view.View
import android.widget.GridView
import renetik.android.base.layout
import renetik.android.controller.base.CSViewController
import renetik.android.controller.common.CSNavigationController
import renetik.android.controller.common.CSNavigationItem
import renetik.android.dialog.extensions.dialog
import renetik.android.extensions.textView
import renetik.android.extensions.view
import renetik.android.listview.CSListController
import renetik.android.listview.CSRowView
import renetik.android.material.extensions.snackBarInfo
import renetik.android.themes.CSThemes.Companion.applyTheme
import renetik.android.themes.CSThemes.Companion.availableThemes
import renetik.android.themes.CSThemes.Companion.currentTheme
import renetik.android.themes.CSThemes.Companion.currentThemeIndex
import renetik.android.view.extensions.background
import renetik.android.view.extensions.title

class CSThemeChooserController(val navigation: CSNavigationController,
                               title: String = "Theme Chooser")
    : CSViewController<View>(navigation, layout(R.layout.theme_chooser)), CSNavigationItem {

    init {
        textView(R.id.ThemeSwitcher_Title).title(currentTheme.title)
        CSListController<CSTheme, GridView>(this, R.id.ThemeSwitcher_Grid) {
            CSRowView(this, layout(R.layout.theme_switcher_item), onLoadSwitchItem)
        }.onItemClick(R.id.ThemeSwitcherItem_Button) { row ->
            if (row.index == currentThemeIndex) snackBarInfo("Current theme...")
            else dialog("You selected theme: ${row.data.title}")
                .show("Apply selected theme ?") { applyTheme(activity(), row.index) }
        }.reload(availableThemes).apply { currentThemeIndex?.let { selectedIndex(it) } }
    }

    private val onLoadSwitchItem: (CSRowView<CSTheme>.(CSTheme) -> Unit) = { theme ->
        val attributes = obtainStyledAttributes(theme.style, R.styleable.Theme)
        view(R.id.ThemeSwitcherItem_ColorPrimary)
            .background(attributes.getColor(R.styleable.Theme_colorPrimary, 0))
        view(R.id.ThemeSwitcherItem_ColorPrimaryVariant)
            .background(attributes.getColor(R.styleable.Theme_colorPrimaryVariant, 0))
        view(R.id.ThemeSwitcherItem_ColorSecondary)
            .background(attributes.getColor(R.styleable.Theme_colorSecondary, 0))
        view(R.id.ThemeSwitcherItem_ColorSecondaryVariant)
            .background(attributes.getColor(R.styleable.Theme_colorSecondaryVariant, 0))
        view(R.id.ThemeSwitcherItem_ColorOnPrimary)
            .background(attributes.getColor(R.styleable.Theme_colorOnPrimary, 0))
        view(R.id.ThemeSwitcherItem_ColorOnSecondary)
            .background(attributes.getColor(R.styleable.Theme_colorOnSecondary, 0))
        attributes.recycle()
        textView(R.id.ThemeSwitcherItem_Title).title(theme.title)
    }

    override val navigationItemTitle = title
}