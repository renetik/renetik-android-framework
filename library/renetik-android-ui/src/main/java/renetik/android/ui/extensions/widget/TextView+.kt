@file:Suppress("unused")

package renetik.android.ui.extensions.widget

import android.text.Editable
import android.widget.TextView
import androidx.annotation.StringRes
import renetik.android.core.extensions.content.drawable
import renetik.android.core.kotlin.asString
import renetik.android.core.lang.CSHasDrawable
import renetik.android.core.lang.value.CSValue
import renetik.android.event.registration.CSHasChangeValue
import renetik.android.event.registration.action
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.event.registration.action
import renetik.android.event.registration.actionNullableChild
import renetik.android.ui.extensions.view.goneIf
import renetik.android.ui.view.adapter.CSTextWatcherAdapter

fun <T : TextView> T.textPrepend(value: CharSequence?) = text("$value$text")
fun <T : TextView> T.textAppend(value: CharSequence?) = text("$text$value")
fun <T : TextView> T.text(value: CSValue<*>) = value(value.value)
fun <T : TextView> T.value(value: Any?) = text(value.asString)

fun <T : TextView> T.text(@StringRes stringId: Int) =
    apply { text = context.getString(stringId) }

fun <T : TextView> T.text(string: CharSequence?) = apply { text = string }
fun <T : TextView> T.text() = text.toString()

fun <T : TextView> T.goneIfBlank() = goneIf(text.isNullOrBlank())

inline fun <T : TextView> T.onTextChange(
    crossinline onChange: (view: T) -> Unit
) = apply {
    addTextChangedListener(object : CSTextWatcherAdapter() {
        override fun afterTextChanged(editable: Editable) = onChange(this@onTextChange)
    })
}

inline fun <T : TextView> T.onFocusChange(
    crossinline onChange: (view: T) -> Unit
) = apply {
    setOnFocusChangeListener { _, _ -> onChange(this) }
}

@JvmName("TextViewTextStringProperty")
fun TextView.text(property: CSHasChangeValue<String>) = text(property, text = { it })

inline fun <T, V> TextView.text(
    parent: CSHasChangeValue<T>,
    crossinline child: (T) -> CSHasChangeValue<V>,
    noinline text: (V) -> Any
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

inline fun <T, V> TextView.textNullableChild(
    parent: CSHasChangeValue<T>,
    crossinline child: (T) -> CSHasChangeValue<V>?,
    noinline text: (V?) -> Any
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

@JvmName("textPropertyChildTextProperty")
inline fun <T> TextView.text(
    parent: CSHasChangeValue<T>, crossinline child: (T) -> CSHasChangeValue<String>
) = this.text(parent, child) { it }

inline fun <ParentValue, ParentChildValue, ChildValue> TextView.textNullableChild(
    parent: CSHasChangeValue<ParentValue>,
    crossinline parentChild: (ParentValue) -> CSHasChangeValue<ParentChildValue>?,
    crossinline child: (ParentChildValue) -> CSHasChangeValue<ChildValue>?,
    crossinline text: (ChildValue?) -> Any
): CSRegistration {
    var childRegistration: CSRegistration? = null
    val parentRegistration: CSRegistration = parent.actionNullableChild(
        child = parentChild, onChange = { parentChildValue ->
            childRegistration?.cancel()
            child(parentChildValue)?.let { childValue ->
                childRegistration = text(childValue, text = { text(it) })
            } ?: value(text(null))
        })
//    parent.value?.let { parentValue ->
//        parentChild(parentValue)?.value?.let { parentChildValue ->
//            value(text(child(parentChildValue)?.value))
//        } ?: value(text(null))
//    } ?: value(text(null))
    return CSRegistration(isActive = true, onCancel = {
        parentRegistration.cancel()
        childRegistration?.cancel()
    })
}

inline fun <T> TextView.text(
    property: CSHasChangeValue<T>, crossinline text: (T) -> Any
): CSRegistration = property.action { value(text(property.value)) }

fun TextView.text(property: CSHasChangeValue<*>): CSRegistration =
    text(property, text = { it.asString })

// Inline dont support local functions
fun <T, V> TextView.text(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<V>,
    text: (T, V) -> Any
): CSRegistration {
    fun apply() = value(text(property1.value, property2.value))
    apply()
    return CSRegistration(
        property1.onChange { apply() },
        property2.onChange { apply() }
    )
}

// Inline dont support local functions
fun <T, V, K> TextView.text(
    property1: CSHasChangeValue<T>,
    property2: CSHasChangeValue<V>,
    property3: CSHasChangeValue<K>,
    text: (T, V, K) -> Any
): CSRegistration {
    fun apply() = value(text(property1.value, property2.value, property3.value))
    apply()
    return CSRegistration(
        property1.onChange { apply() },
        property2.onChange { apply() },
        property3.onChange { apply() },
    )
}

fun <T : CSHasDrawable> TextView.drawableStart(property: CSHasChangeValue<T>) =
    property.action { drawable(start = context.drawable(property.value.drawable)) }

inline fun <T> TextView.drawableStart(
    property: CSHasChangeValue<T>, crossinline getDrawable: (T) -> Int?
) = property.action { drawable(start = getDrawable(property.value)?.let(context::drawable)) }

fun <T : TextView> T.lines(max: Int) = apply { maxLines = max }
