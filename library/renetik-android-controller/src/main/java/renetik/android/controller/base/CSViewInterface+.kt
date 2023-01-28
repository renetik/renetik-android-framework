package renetik.android.controller.base

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.IdRes
import androidx.drawerlayout.widget.DrawerLayout
import renetik.android.core.kotlin.notNull
import renetik.android.event.common.CSHasDestruct
import renetik.android.event.common.destruct
import renetik.android.event.property.start
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.event.registration.cancel
import renetik.android.event.registration.listenOnce
import renetik.android.event.registration.register
import renetik.android.ui.extensions.view.*
import renetik.android.ui.extensions.widget.onChange
import renetik.android.ui.extensions.widget.radioGroup
import renetik.android.ui.protocol.CSViewInterface




