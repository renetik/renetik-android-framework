package renetik.android.framework.items

import renetik.android.controller.view.grid.RecyclerViewOut
import renetik.android.core.lang.value.isFalse
import renetik.android.framework.items.CSSectionItem.Companion.EmptyViewId
import renetik.android.framework.items.CSSectionItem.Companion.HeaderViewId
import renetik.android.framework.items.CSSectionItem.Companion.ItemViewId

fun <Item> RecyclerViewOut<CSSectionItem<CSItemSection<Item>, Item>>.reload(
    sections: List<CSItemSection<Item>>,
    filter: ((items: List<Item>) -> List<Item>)? = null) {
    val items = mutableListOf<Pair<CSSectionItem<CSItemSection<Item>, Item>, Int>>()
    sections.forEachIndexed { index, section -> items.load(section, index, filter) }
    reload(items)
}

fun <Item> MutableList<Pair<CSSectionItem<CSItemSection<Item>, Item>, Int>>.load(
    section: CSItemSection<Item>, index: Int,
    filter: ((items: List<Item>) -> List<Item>)? = null) {
    this += CSSectionItem<CSItemSection<Item>, Item>(section,
        index) to HeaderViewId
    if (section.isCollapsed.isFalse) {
        if (section.items.isEmpty()) this += CSSectionItem<CSItemSection<Item>, Item>(
            section,
            index) to EmptyViewId
        else (filter?.invoke(section.items)
            ?: section.items).forEachIndexed { itemIndex, item ->
            this += CSSectionItem(section, itemIndex, item) to ItemViewId
        }
    }
}

