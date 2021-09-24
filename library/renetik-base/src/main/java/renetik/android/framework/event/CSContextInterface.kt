package renetik.android.framework.event

import android.content.Context
import renetik.android.framework.CSEventOwnerHasDestroy

interface CSContextInterface : CSEventOwnerHasDestroy {
    val context: Context
}