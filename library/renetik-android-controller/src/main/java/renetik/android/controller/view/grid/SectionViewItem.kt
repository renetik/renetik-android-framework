package renetik.android.controller.view.grid

data class SectionViewItem<Item>(
    val section: String? = null,
    val index: Int,
    val item: Item? = null
) {
    companion object {
        const val HeaderViewId = 0
        const val ItemViewId = 1
    }
}