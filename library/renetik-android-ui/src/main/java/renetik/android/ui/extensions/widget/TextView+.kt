package renetik.android.ui.extensions.widget

import android.text.Editable
import android.widget.TextView
import renetik.android.core.extensions.content.drawable
import renetik.android.core.kotlin.asString
import renetik.android.core.lang.CSHasDrawable
import renetik.android.core.lang.value.CSValue
import renetik.android.event.property.CSProperty
import renetik.android.event.property.action
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.ui.extensions.view.shownIf
import renetik.android.ui.view.adapter.CSTextWatcherAdapter

fun <T : TextView> T.textPrepend(value: CharSequence?) = text("$value$text")
fun <T : TextView> T.textAppend(value: CharSequence?) = text("$text$value")
fun <T : TextView> T.text(value: CSValue<*>) = text(value.value)
fun <T : TextView> T.value(value: Any?) = text(value.asString)
fun <T : TextView> T.text(value: Any?) = text(value.asString)
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
fun TextView.text(property: CSProperty<String>) = text(property, text = { it })

fun <T, V> TextView.text(parent: CSProperty<T>,
                         child: (T) -> CSProperty<V>,
                         text: (V) -> Any): CSRegistration {
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
    parent: CSProperty<T>, child: (T) -> CSProperty<String>) =
    this.text(parent, child) { it }

fun <T, V> TextView.textNullableChild(
    parent: CSProperty<T>, child: (T) -> CSProperty<V>?,
    text: (V?) -> Any): CSRegistration {
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

fun <T> TextView.text(property: CSProperty<T>, text: (T) -> Any) =
    property.action { value(text(property.value)) }

fun TextView.text(property: CSProperty<*>) = text(property, text = { it.asString })

fun <T, V> TextView.text(property1: CSProperty<T>, property2: CSProperty<V>,
                         text: (T, V) -> Any): CSRegistration {
    fun update() = value(text(property1.value, property2.value))
    update()
    return CSRegistration(
        property1.onChange { update() },
        property2.onChange { update() })
}

fun <T : CSHasDrawable> TextView.startDrawable(property: CSProperty<T>) =
    property.action { startDrawable(context.drawable(property.value.drawable)) }

fun <T> TextView.startDrawable(property: CSProperty<T>, getDrawable: (T) -> Int?) =
    property.action { startDrawable(getDrawable(property.value)?.let(context::drawable)) }