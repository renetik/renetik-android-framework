package renetik.android.controller.view.grid.item

data class CSSectionItem<Section, Item>(
    val section: Section,
    val index: Int,
    val item: Item? = null
) {
    companion object {
        const val SectionHeaderViewId = -1
        const val HeaderViewId = 0
        const val ItemViewId = 1
        const val EmptyViewId = 2
    }
}