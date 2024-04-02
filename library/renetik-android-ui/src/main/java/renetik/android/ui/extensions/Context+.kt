package renetik.android.ui.extensions

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import renetik.android.ui.protocol.CSViewInterface

@Suppress("UNCHECKED_CAST")
fun <ViewType : View> Context.inflate(layoutId: Int, group: ViewGroup? = null): ViewType {
    val context = CSViewInterface.themeOverride
        ?.let { ContextThemeWrapper(this, it) } ?: this
    return LayoutInflater.from(context).inflate(layoutId, group, false) as ViewType
}