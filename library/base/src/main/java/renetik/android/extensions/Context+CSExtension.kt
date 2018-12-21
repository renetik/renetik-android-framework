package renetik.android.extensions

import android.content.Context
import android.view.LayoutInflater
import android.view.View

@Suppress("UNCHECKED_CAST")
fun <ViewType : View> Context.inflate(layoutId: Int) =
        LayoutInflater.from(this).inflate(layoutId, null) as ViewType

fun Context.applicationLabel() = applicationContext.stringFromAttribute(android.R.attr.label)