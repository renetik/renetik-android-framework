package renetik.android.framework.event

import android.content.Context
import renetik.android.framework.base.CSEventOwnerHasDestroy

interface CSHasContext : CSEventOwnerHasDestroy {
    val context: Context
}