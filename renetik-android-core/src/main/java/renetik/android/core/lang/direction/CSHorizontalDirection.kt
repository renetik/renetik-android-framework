package renetik.android.core.lang.direction

import renetik.android.core.R
import renetik.android.core.base.CSApplication.Companion.getString

enum class CSHorizontalDirection(override val title: String) : CSDirection {
    Start(getString(R.string.cs_direction_horizontal_start)),
    End(getString(R.string.cs_direction_horizontal_end));

    override val isHorizontal: Boolean = true
}