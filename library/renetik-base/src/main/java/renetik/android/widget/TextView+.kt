package renetik.android.widget

import android.text.Editable
import android.widget.TextView
import renetik.android.content.drawable
import renetik.android.framework.event.CSRegistration
import renetik.android.framework.event.CSRegistration.Companion.registration
import renetik.android.framework.event.CSMultiEventRegistration
import renetik.android.framework.event.CSVisibleEventOwner
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.event.property.action
import renetik.android.framework.lang.CSHasDrawable
import renetik.android.framework.lang.CSValue
import renetik.android.framework.view.adapter.CSTextWatcherAdapter
import renetik.android.view.shownIf
import renetik.kotlin.asString

fun <T : TextView> T.textPrepend(string: CharSequence?) = text("$string$title")
fun <T : TextView> T.textAppend(string: CharSequence?) = text("$title$string")
fun <T : TextView> T.text(value: CSValue<*>) = text(value.value.asString)
fun <T : TextView> T.text(value: Any?) = text(value.asString)
fun <T : TextView> T.text(string: CharSequence?) = apply { text = string }
fun <T : TextView> T.text() = text.asString

var <T : TextView> T.title: String
    get() = text()
    set(value) {
        text(value)
    }

fun <T : TextView> T.goneIfEmpty() = apply { shownIf(text().isBlank()) }

fun <T : TextView> T.onTextChange(onChange: (view: T) -> Unit) = apply {
    addTextChangedListener(object : CSTextWatcherAdapter() {
        override fun afterTextChanged(editable: Editable) = onChange(this@onTextChange)
    })
}

fun <T : TextView> T.onFocusChange(onChange: (view: T) -> Unit) = apply {
    setOnFocusChangeListener { _, _ -> onChange(this) }
}

fun TextView.text(parent: CSVisibleEventOwner, property: CSEventProperty<*>) =
    text(parent, property) { it.asString }

@JvmName("TextViewTextStringProperty")
fun TextView.text(parent: CSVisibleEventOwner, property: CSEventProperty<String>) =
    text(parent, property) { it }

fun <T> TextView.text(
    parent: CSVisibleEventOwner, property: CSEventProperty<T>,
    getText: (T) -> CharSequence
) = apply {
    fun updateText() = text(getText(property.value))
    parent.whileShowing(property.onChange { updateText() })
    updateText()
}

@JvmName("TextViewTextStringProperty")
fun TextView.text(property: CSEventProperty<String>) = text(property) { it }

fun <T, V> TextView.text(parent: CSEventProperty<T>,
                         child: (T) -> CSEventProperty<V>,
                         getText: (V) -> Any): CSRegistration {
    var childRegistration: CSRegistration? = null
    val parentRegistration = parent.action {
        childRegistration?.cancel()
        childRegistration = text(child(parent.value), getText)
    }
    return registration {
        parentRegistration.cancel()
        childRegistration?.cancel()
    }
}

fun <T> TextView.textOfChild(
    parent: CSEventProperty<T>, child: (T) -> CSEventProperty<String>) =
    text(parent, child) { it }

fun <T, V> TextView.textNullableChild(
    parent: CSEventProperty<T>, child: (T) -> CSEventProperty<V>?,
    getText: (V?) -> Any): CSRegistration {
    var childRegistration: CSRegistration? = null
    val parentRegistration = parent.action {
        childRegistration?.cancel()
        childRegistration = child(parent.value)?.let { text(it, getText) }
        if (childRegistration == null) text(getText(null))
    }
    return registration {
        parentRegistration.cancel()
        childRegistration?.cancel()
    }
}

fun <T> TextView.text(property: CSEventProperty<T>, getText: (T) -> Any) =
    property.action { text(getText(property.value)) }

fun TextView.text(property: CSEventProperty<*>) = text(property, getText = { it.asString })

fun <T, V> TextView.text(property1: CSEventProperty<T>, property2: CSEventProperty<V>,
                         getText: (T, V) -> Any): CSRegistration {
    fun update() = text(getText(property1.value, property2.value))
    update()
    return CSMultiEventRegistration(
        property1.onChange { update() },
        property2.onChange { update() })
}

fun <T : CSHasDrawable> TextView.startDrawable(property: CSEventProperty<T>) =
    property.action { startDrawable(context.drawable(property.value.drawable)) }

fun <T> TextView.startDrawable(property: CSEventProperty<T>, getDrawable: (T) -> Int?) =
    property.action { startDrawable(getDrawable(property.value)?.let(context::drawable)) }