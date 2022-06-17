package renetik.android.event.owner

import android.content.Context
import renetik.android.event.owner.CSEventOwnerHasDestroy

interface CSHasContext : CSEventOwnerHasDestroy {
    val context: Context
}