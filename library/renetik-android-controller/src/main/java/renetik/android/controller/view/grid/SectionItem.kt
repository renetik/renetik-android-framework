package renetik.android.controller.view.grid

data class SectionItem<Section, Item>(
    val section: Section,
    val index: Int,
    val item: Item? = null
) {
    companion object {
        const val HeaderViewId = 0
        const val ItemViewId = 1
        const val EmptyViewId = 2
    }
}