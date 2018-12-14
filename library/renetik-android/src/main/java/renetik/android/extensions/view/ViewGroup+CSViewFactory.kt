package renetik.android.extensions.view

import android.view.ContextThemeWrapper
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.TextView
import com.google.android.material.card.MaterialCardView

fun ViewGroup.FrameLayout(layout: LayoutParams, init: ((FrameLayout).() -> Unit)? = null) =
        add(context.FrameLayout(), layout, init)

fun ViewGroup.MaterialCardView(layout: LayoutParams, init: ((MaterialCardView).() -> Unit)? = null) =
        add(context.MaterialCardView(), layout, init)

fun ViewGroup.MaterialCardView(layout: LayoutParams, style: Int, init: ((MaterialCardView).() -> Unit)? = null) =
        add(ContextThemeWrapper(context, style).MaterialCardView(style), layout, init)

fun ViewGroup.TextView(params: LayoutParams, title: String, textStyleResource: Int,
                       styleResource: Int, init: (TextView.() -> Unit)? = null) =
        add(context.TextView(title, textStyleResource, styleResource), params, init)

fun ViewGroup.TextView(params: LayoutParams, textStyleResource: Int, styleResource: Int,
                       init: (TextView.() -> Unit)? = null) =
        TextView(params, "", textStyleResource, styleResource, init)

fun ViewGroup.Button(params: LayoutParams, title: String, textStyleResource: Int,
                     styleResource: Int, init: (Button.() -> Unit)? = null) =
        add(context.Button(title, textStyleResource, styleResource), params, init)

fun ViewGroup.Button(params: LayoutParams, title: String, textStyleResource: Int,
                     init: (Button.() -> Unit)? = null) =
        add(context.Button(title, textStyleResource, null), params, init)

fun ViewGroup.ImageView(params: LayoutParams, src: Int, scale: ScaleType = ScaleType.CENTER_INSIDE,
                        styleResource: Int? = null, init: (ImageView.() -> Unit)? = null) =
        add(context.ImageView(scale, src, styleResource), params, init)

