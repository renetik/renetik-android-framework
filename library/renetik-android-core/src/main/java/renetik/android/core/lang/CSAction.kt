package renetik.android.core.lang

import renetik.android.core.base.CSApplication.Companion.getString

data class CSAction(val title: String, val onClick: () -> Unit) {
    constructor(title: Int, onClick: () -> Unit) : this(getString(title), onClick)
}
