package renetik.android.themes

import android.view.View
import android.widget.GridView
import renetik.android.base.application
import renetik.android.base.layout
import renetik.android.controller.base.CSViewController
import renetik.android.controller.common.CSNavigationController
import renetik.android.controller.common.CSNavigationItem
import renetik.android.dialog.extensions.dialog
import renetik.android.extensions.simpleView
import renetik.android.extensions.textView
import renetik.android.view.extensions.background
import renetik.android.view.extensions.title
import renetik.android.listview.CSListController
import renetik.android.listview.CSRowView
import renetik.android.java.collections.CSList
import renetik.android.java.collections.list

var currentThemeIndex: Int? = null
var availableThemes: CSList<Theme> = list()

class CSThemeSwitcherController(val navigation: CSNavigationController)
    : CSViewController<View>(navigation, layout(R.layout.theme_switcher)), CSNavigationItem {

    init {
        CSListController<Theme, GridView>(this, R.id.ThemeSwitcher_Grid) {
            CSRowView(this, layout(R.layout.theme_switcher_item), onLoadSwitchItem)
        }.onItemClick(R.id.ThemeSwitcherItem_Button) { row ->
            dialog("You selected other theme").show("Apply theme ?") { applyTheme(row.index) }
        }.reload(availableThemes)
    }

    private fun applyTheme(themeIndex: Int) {
        if (currentThemeIndex == themeIndex) return
        application.store.put("theme_index", themeIndex)
        activity().recreate()
        navigation.pop()
    }

    private val onLoadSwitchItem: (CSRowView<Theme>.(Theme) -> Unit) = { theme ->
        val attributes = obtainStyledAttributes(theme.style, R.styleable.Theme)
        simpleView(R.id.ThemeSwitcherItem_ColorPrimary)
                .background(attributes.getColor(R.styleable.Theme_colorPrimary, 0))
        simpleView(R.id.ThemeSwitcherItem_ColorPrimaryVariant)
                .background(attributes.getColor(R.styleable.Theme_colorPrimaryVariant, 0))
        simpleView(R.id.ThemeSwitcherItem_ColorSecondary)
                .background(attributes.getColor(R.styleable.Theme_colorSecondary, 0))
        simpleView(R.id.ThemeSwitcherItem_ColorSecondaryVariant)
                .background(attributes.getColor(R.styleable.Theme_colorSecondaryVariant, 0))
        simpleView(R.id.ThemeSwitcherItem_ColorOnPrimary)
                .background(attributes.getColor(R.styleable.Theme_colorOnPrimary, 0))
        simpleView(R.id.ThemeSwitcherItem_ColorOnSecondary)
                .background(attributes.getColor(R.styleable.Theme_colorOnSecondary, 0))
        attributes.recycle()
        textView(R.id.ThemeSwitcherItem_Title).title(theme.title)
    }
}