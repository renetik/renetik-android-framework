package renetik.android.framework.event

import android.content.Context

interface CSContextInterface : CSHasDestroy, CSEventOwner {
    val context: Context
}