package renetik.android.controller.view.grid

data class SectionDialogItem<Item>(
    val section: String? = null,
    val index: Int,
    val item: Item? = null)