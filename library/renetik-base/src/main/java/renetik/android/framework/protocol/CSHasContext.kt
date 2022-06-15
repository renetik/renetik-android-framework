package renetik.android.framework.protocol

import android.content.Context
import renetik.android.framework.protocol.CSEventOwnerHasDestroy

interface CSHasContext : CSEventOwnerHasDestroy {
    val context: Context
}