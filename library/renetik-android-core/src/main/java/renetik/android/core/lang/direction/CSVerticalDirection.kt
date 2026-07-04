package renetik.android.core.lang.direction

import renetik.android.core.R
import renetik.android.core.base.CSApplication.Companion.getString

enum class CSVerticalDirection(override val title: String) : CSDirection {
    Bottom(getString(R.string.cs_direction_vertical_bottom)),
    Top(getString(R.string.cs_direction_vertical_top));

    override val isHorizontal: Boolean = false
}