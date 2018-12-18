package renetik.android.extensions.view

import android.content.Context
import android.view.ViewGroup
import android.widget.*
import android.widget.ImageView.ScaleType
import com.airbnb.paris.extensions.style
import com.google.android.material.card.MaterialCardView
import org.jetbrains.anko.textAppearance

fun Context.FrameLayout(init: ((FrameLayout).() -> Unit)? = null) = FrameLayout(this).apply {
    init?.invoke(this)
}

fun Context.FrameLayout(styleResource: Int, init: ((FrameLayout).() -> Unit)? = null) = FrameLayout(this).apply {
    style(styleResource)
    init?.invoke(this)
}

fun Context.HorizontalLayout(styleResource: Int, init: ((LinearLayout).() -> Unit)? = null) = LinearLayout(this).apply {
    style(styleResource)
    orientation = LinearLayout.HORIZONTAL
    init?.invoke(this)
}

fun Context.VerticalLayout(styleResource: Int, init: ((LinearLayout).() -> Unit)? = null) = LinearLayout(this).apply {
    style(styleResource)
    orientation = LinearLayout.VERTICAL
    init?.invoke(this)
}

fun Context.MaterialCardView(init: ((MaterialCardView).() -> Unit)? = null) = MaterialCardView(this).apply {
    init?.invoke(this)
}

fun Context.MaterialCardView(styleResource: Int, init: ((MaterialCardView).() -> Unit)? = null) = MaterialCardView(this).apply {
    style(styleResource)
    init?.invoke(this)
}

fun Context.MaterialCardView(layout: ViewGroup.LayoutParams, styleResource: Int, init: ((MaterialCardView).() -> Unit)? = null) = MaterialCardView(this).apply {
    layoutParams = layout
    style(styleResource)
    init?.invoke(this)
}

fun Context.ImageView(scale: ScaleType = ScaleType.CENTER_INSIDE, resource: Int? = null, style: Int? = null,
                      init: (ImageView.() -> Unit)? = null) = ImageView(this).apply {
    scaleType = scale
    resource?.let { image(resource) }
    style?.let { style(it) }
    init?.invoke(this)
}

fun Context.TextView(textStyle: Int?, style: Int?, init: ((TextView).() -> Unit)? = null) = TextView(null, textStyle, style, init)
fun Context.TextView(title: String? = null, init: ((TextView).() -> Unit)? = null) = TextView(title, null, null, init)
fun Context.TextView(title: String, textStyle: Int, init: ((TextView).() -> Unit)? = null) = TextView(title, textStyle, null, init)
fun Context.TextView(title: String? = null, textStyle: Int? = null, style: Int? = null, init: (TextView.() -> Unit)? = null) =
        TextView(this).apply {
            text = title
            textStyle?.let { textAppearance = it }
            style?.let { style(it) }
            init?.invoke(this)
        }

fun Context.Button(title: String? = null, init: ((Button).() -> Unit)? = null) = Button(title, null, null, init)
fun Context.Button(title: String?, textStyle: Int?, style: Int?, init: (Button.() -> Unit)? = null) =
        Button(this).apply {
            text = title
            textStyle?.let { textAppearance = it }
            style?.let { style(it) }
            init?.invoke(this)
        }


fun Context.ListView(style: Int? = null, init: ((ListView).() -> Unit)? = null) = ListView(this).apply {
    style?.let { style(it) }
    init?.invoke(this)
}