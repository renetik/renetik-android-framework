package renetik.android.controller.view.grid.item

data class CSSectionItem<Section, Item>(
    val section: Section,
    val index: Int,
    val item: Item? = null
) {
    companion object {
        const val ItemViewId = 0
        const val ItemSectionViewId = 1
        const val ItemSectionEmptyViewId = 2
        const val ItemSectionHeaderViewId = 3
    }
}