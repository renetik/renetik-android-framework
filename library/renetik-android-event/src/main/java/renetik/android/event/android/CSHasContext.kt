package renetik.android.event.android

import android.content.Context
import renetik.android.event.lifecycle.CSHasRegistrationsHasDestruct

interface CSHasContext : CSHasRegistrationsHasDestruct {
    val context: Context
}