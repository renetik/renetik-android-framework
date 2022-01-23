package renetik.android.widget

import android.text.Editable
import android.widget.TextView
import androidx.annotation.StringRes
import renetik.android.content.drawable
import renetik.android.framework.event.*
import renetik.android.framework.event.CSEvent.Companion.event
import renetik.android.framework.event.CSEventRegistration.Companion.registration
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.lang.CSDrawableInterface
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

fun TextView.text(property: CSEventProperty<*>) = text(property) { it.asString }

@JvmName("TextViewTextStringProperty")
fun TextView.text(property: CSEventProperty<String>) = text(property) { it }

fun <T, V> TextView.text(parent: CSEventProperty<T>,
                         child: (T) -> CSEventProperty<V>,
                         getText: (V) -> Any): CSEventRegistration {
    var childRegistration = text(child(parent.value), getText)
    val parentRegistration = parent.onChange {
        childRegistration.cancel()
        childRegistration = text(child(parent.value), getText)
    }
    return registration {
        parentRegistration.cancel()
        childRegistration.cancel()
    }
}


fun <T> TextView.text(property: CSEventProperty<T>, getText: (T) -> Any)
        : CSEventRegistration {
    fun update() = text(getText(property.value))
    update()
    return property.onChange { update() }
}


fun <T, V> TextView.text(property1: CSEventProperty<T>, property2: CSEventProperty<V>,
                         getText: (T, V) -> Any): CSEventRegistration {
    fun update() = text(getText(property1.value, property2.value))
    update()
    return CSMultiEventRegistration(
        property1.onChange { update() },
        property2.onChange { update() })
}

fun <T : CSDrawableInterface> TextView.startDrawable(property: CSEventProperty<T>)
        : CSEventRegistration {
    fun updateDrawable() = startDrawable(context.drawable(property.value.drawable))
    updateDrawable()
    return property.onChange { updateDrawable() }
}