package renetik.android.framework.protocol

import android.content.Context
import renetik.android.event.owner.CSEventOwnerHasDestroy

interface CSHasContext : CSEventOwnerHasDestroy {
    val context: Context
}