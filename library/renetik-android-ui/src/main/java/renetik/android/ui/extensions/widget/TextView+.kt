package renetik.android.ui.extensions.widget

import android.text.Editable
import android.widget.TextView
import androidx.annotation.StringRes
import renetik.android.core.extensions.content.drawable
import renetik.android.core.kotlin.asString
import renetik.android.core.kotlin.primitives.asIndex
import renetik.android.core.lang.CSHasDrawable
import renetik.android.core.lang.CSStringConstants.Ellipsize
import renetik.android.core.lang.value.CSValue
import renetik.android.event.property.action
import renetik.android.event.registration.CSHasChangeValue
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.event.registration.later
import renetik.android.ui.extensions.view.*
import renetik.android.ui.view.adapter.CSTextWatcherAdapter

fun <T : TextView> T.textPrepend(value: CharSequence?) = text("$value$text")
fun <T : TextView> T.textAppend(value: CharSequence?) = text("$text$value")
fun <T : TextView> T.text(value: CSValue<*>) = value(value.value)
fun <T : TextView> T.value(value: Any?) = text(value.asString)

fun <T : TextView> T.text(@StringRes stringId: Int) =
    apply { text = context.getString(stringId) }

fun <T : TextView> T.text(string: CharSequence?) = apply { text = string }
fun <T : TextView> T.text() = text.toString()
fun <T : TextView> T.goneIfEmpty() = apply { shownIf(text().isBlank()) }

fun <T : TextView> T.onTextChange(onChange: (view: T) -> Unit) = apply {
    addTextChangedListener(object : CSTextWatcherAdapter() {
        override fun afterTextChanged(editable: Editable) = onChange(this@onTextChange)
    })
}

fun <T : TextView> T.onFocusChange(onChange: (view: T) -> Unit) = apply {
    setOnFocusChangeListener { _, _ -> onChange(this) }
}

@JvmName("TextViewTextStringProperty")
fun TextView.text(property: CSHasChangeValue<String>) = text(property, text = { it })

fun <T, V> TextView.text(
    parent: CSHasChangeValue<T>,
    child: (T) -> CSHasChangeValue<V>,
    text: (V) -> Any
): CSRegistration {
    var childRegistration: CSRegistration? = null
    val parentRegistration = parent.action {
        childRegistration?.cancel()
        childRegistration = text(child(parent.value), text)
    }
    return CSRegistration(isActive = true, onCancel = {
        parentRegistration.cancel()
        childRegistration?.cancel()
    })
}

@JvmName("textPropertyChildTextProperty")
fun <T> TextView.text(
    parent: CSHasChangeValue<T>, child: (T) -> CSHasChangeValue<String>
) =
    this.text(parent, child) { it }

fun <T, V> TextView.textNullableChild(
    parent: CSHasChangeValue<T>, child: (T) -> CSHasChangeValue<V>?,
    text: (V?) -> Any
): CSRegistration {
    var childRegistration: CSRegistration? = null
    val parentRegistration = parent.action {
        childRegistration?.cancel()
        childRegistration = child(parent.value)?.let { text(it, text) }
        if (childRegistration == null) value(text(null))
    }
    return CSRegistration(isActive = true, onCancel = {
        parentRegistration.cancel()
        childRegistration?.cancel()
    })
}

fun <T> TextView.text(property: CSHasChangeValue<T>, text: (T) -> Any) =
    property.action { value(text(property.value)) }

fun TextView.text(property: CSHasChangeValue<*>): CSRegistration =
    text(property, text = { it.asString })

fun <T, V> TextView.text(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<V>,
    text: (T, V) -> Any
): CSRegistration {
    fun update() = value(text(property1.value, property2.value))
    update()
    return CSRegistration(
        property1.onChange { update() },
        property2.onChange { update() })
}

fun <T : CSHasDrawable> TextView.startDrawable(property: CSHasChangeValue<T>) =
    property.action { startDrawable(context.drawable(property.value.drawable)) }

fun <T> TextView.startDrawable(property: CSHasChangeValue<T>, getDrawable: (T) -> Int?) =
    property.action { startDrawable(getDrawable(property.value)?.let(context::drawable)) }

fun TextView.ellipsize(parent: CSHasRegistrations, lines: Int) = apply {
    fun update() {
        if (layout == null) return
        if (layout.lineCount > lines)
            text = text().substring(0, layout.getLineEnd(lines.asIndex) - 1) + Ellipsize
    }
    invisible()
    parent.later(after = 0) {
        update()
        visible()
        onTextChange { update() }
        onBoundsChange { update() }
    }
}

fun TextView.ellipsize(parent: CSHasRegistrations) = apply {
    val maxLines = this.maxLines
    if (maxLines <= 0) return@apply
    this.maxLines = Int.MAX_VALUE
    ellipsize(parent, maxLines)
}