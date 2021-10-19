package renetik.android.framework.event

import android.content.Context
import renetik.android.framework.CSEventOwnerHasDestroy

interface CSHasContext : CSEventOwnerHasDestroy {
    val context: Context
}