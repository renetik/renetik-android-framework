package renetik.android.controller.view.grid.item

import renetik.android.store.CSStore
import renetik.android.store.CSStore.Companion.EmptyStore
import renetik.android.store.CSStore.Companion.runtimeStore
import renetik.android.store.extensions.property

data class CSItemSection<Item>(
    val id: String,
    val title: String,
    val description: String = "",
    var items: List<Item> = emptyList(),
    val store: CSStore = EmptyStore,
    val isCollapsedDefault: Boolean = false
) {
    val isCollapsed = store.property(id, isCollapsedDefault)
    var header: String? = null
    fun header(title: String) = apply { header = title }

    constructor(
        title: String, description: String = "", items: List<Item>
    ) : this(title, title, description, items, runtimeStore)

    companion object {
        fun <Item> dialogSection(
            title: String, subtitle: String, vararg items: Item
        ) = CSItemSection(title, subtitle, items.asList())

        inline fun <reified Item> dialogSection(
            title: String, subtitle: String = "", items: List<Item>
        ) = CSItemSection(title, subtitle, items)

        inline fun <reified Item> dialogSection(
            store: CSStore, id: String, title: String,
            items: List<Item>, isDefaultCollapsed: Boolean = false
        ) = CSItemSection(id, title, "", items, store, isDefaultCollapsed)

        fun <Item> dialogSection(
            title: String, vararg items: Item
        ) = CSItemSection(title, "", items.asList())
    }
}