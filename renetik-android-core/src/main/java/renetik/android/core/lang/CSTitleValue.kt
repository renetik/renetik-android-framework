package renetik.android.core.lang

import renetik.android.core.lang.value.CSValue

data class CSTitleValue(
    override val title: String, override val value: Int
) : CSHasTitle, CSValue<Int>