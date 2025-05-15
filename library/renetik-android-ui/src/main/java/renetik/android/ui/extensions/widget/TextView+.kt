@file:Suppress("unused")

package renetik.android.ui.extensions.widget

import android.text.Editable
import android.text.method.LinkMovementMethod
import android.text.util.Linkify.EMAIL_ADDRESSES
import android.text.util.Linkify.PHONE_NUMBERS
import android.text.util.Linkify.WEB_URLS
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.text.util.LinkifyCompat
import renetik.android.core.extensions.content.drawable
import renetik.android.core.extensions.content.isPhone
import renetik.android.core.kotlin.asString
import renetik.android.core.kotlin.primitives.vertical
import renetik.android.core.lang.CSHasDrawable
import renetik.android.core.lang.value.CSValue
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.registration.CSHasChangeValue
import renetik.android.event.registration.CSHasChangeValue.Companion.ValueFunction
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.event.registration.action
import renetik.android.ui.R
import renetik.android.ui.extensions.view.gone
import renetik.android.ui.extensions.view.propertyWithTag
import renetik.android.ui.view.adapter.CSTextWatcherAdapter
import kotlin.properties.Delegates.notNull


fun <T : TextView> T.textPrepend(value: CharSequence?) = text("$value$text")
fun <T : TextView> T.textAppend(value: CharSequence?) = text("$text$value")
fun <T : TextView> T.text(value: CSValue<*>) = value(value.value)
fun <T : TextView> T.value(value: Any?) = text(value.asString)

fun <T : TextView> T.text(@StringRes stringId: Int) =
    apply { text = context.getString(stringId) }

fun <T : TextView> T.text(string: CharSequence?) = apply { text = string }
fun <T : TextView> T.text(): String = text.toString()
var <T : TextView> T.string: String
    get() = text()
    set(value) = run { text = value }

fun <T : TextView> T.clearText() = text("")
fun <T : TextView> T.textToVertical() = text("$text".vertical())

fun <T : TextView> T.goneIfBlank() = gone(text.isNullOrBlank())

//LinkifyCompat works just after text is set
//autoLinkMask works but maybe not well on api < 28
fun <T : TextView> T.autoLinkAll() = apply {
    onTextChange { LinkifyCompat.addLinks(this, WEB_URLS or PHONE_NUMBERS or EMAIL_ADDRESSES) }
    movementMethod = LinkMovementMethod.getInstance()
}

fun <T : TextView> T.autoLinkMail() = apply {
    onTextChange { LinkifyCompat.addLinks(this, EMAIL_ADDRESSES) }
    movementMethod = LinkMovementMethod.getInstance()
}

val <T : TextView> T.textChange
    get() = object : CSHasChangeValue<String> {
        override val value: String get() = text()

        override fun onChange(function: (String) -> Unit): CSRegistration {
            val value = ValueFunction(value, function)
            return onTextChange { value(text()) }
        }
    }

inline fun <T : TextView> T.onTextChange(
    crossinline onChange: (view: T) -> Unit
): CSRegistration {
    var registration: CSRegistration by notNull()
    val listener = object : CSTextWatcherAdapter() {
        override fun afterTextChanged(editable: Editable) {
            if (registration.isActive) onChange(this@onTextChange)
        }
    }
    addTextChangedListener(listener)
    registration = CSRegistration(
        isActive = true,
        onCancel = { removeTextChangedListener(listener) },
    )
    return registration
}

val View.eventFocus
    get() = propertyWithTag(R.id.ViewEventOnFocusTag) {
        event<Boolean>().also { setOnFocusChangeListener { _, isFocus -> it.fire(isFocus) } }
    }

inline fun View.onFocusChange(
    crossinline onChange: (hasFocus: Boolean) -> Unit
) = eventFocus.listen { onChange(it) }

inline fun View.onFocusGained(crossinline onFocusGained: () -> Unit) =
    onFocusChange { if (it) onFocusGained() }

inline fun View.onFocusLost(crossinline onFocusLost: () -> Unit) =
    onFocusChange { if (!it) onFocusLost() }

@JvmName("TextViewTextStringProperty")
fun TextView.text(property: CSHasChangeValue<String>) = text(property, text = { it })

inline fun <T, V> TextView.text(
    parent: CSHasChangeValue<T>,
    crossinline child: (T) -> CSHasChangeValue<V>,
    noinline text: (V) -> Any
): CSRegistration = parent.action(child = child, action = { value(text(it)) })

inline fun <T, V> TextView.textNullableChild(
    parent: CSHasChangeValue<T>,
    crossinline child: (T) -> CSHasChangeValue<V>?,
    noinline text: (V?) -> Any
): CSRegistration = parent.action(child, onChange = { value(text(it)) })

@JvmName("textPropertyChildTextProperty")
inline fun <T> TextView.text(
    parent: CSHasChangeValue<T>,
    crossinline child: (T) -> CSHasChangeValue<String>
) = this.text(parent, child) { it }

inline fun <ParentValue, ParentChildValue, ChildValue> TextView.textNullableChild(
    parent: CSHasChangeValue<ParentValue>,
    crossinline parentChild: (ParentValue) -> CSHasChangeValue<ParentChildValue>?,
    crossinline child: (ParentChildValue) -> CSHasChangeValue<ChildValue>?,
    crossinline text: (ChildValue?) -> Any
): CSRegistration {
    var childRegistration: CSRegistration? = null
    val parentRegistration: CSRegistration = parent.action(
        parentChild, onChange = { parentChildValue ->
            childRegistration?.cancel()
            parentChildValue?.let(child)?.let { childValue ->
                childRegistration = text(childValue, text = { text(it) })
            } ?: value(text(null))
        })
    return CSRegistration(isActive = true, onCancel = {
        parentRegistration.cancel()
        childRegistration?.cancel()
    })
}

inline fun <T> TextView.text(
    property: CSHasChangeValue<T>, crossinline text: (T) -> Any
): CSRegistration = property.action { value -> value(text(value)) }

fun TextView.text(property: CSHasChangeValue<*>): CSRegistration =
    text(property, text = Any?::asString)

inline fun <T, V> TextView.text(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<V>,
    crossinline text: (T, V) -> Any
): CSRegistration {
    val value = text(property1.value, property2.value)
    text(value.asString)
    val valueFunction = ValueFunction(value) { text(it.asString) }
    return CSRegistration(
        property1.onChange { valueFunction(text(it, property2.value)) },
        property2.onChange { valueFunction(text(property1.value, it)) },
    )
}

inline fun <T, V, K> TextView.text(
    property1: CSHasChangeValue<T>,
    property2: CSHasChangeValue<V>,
    property3: CSHasChangeValue<K>,
    crossinline text: (T, V, K) -> Any
): CSRegistration {
    val value = text(property1.value, property2.value, property3.value)
    text(value.asString)
    val valueFunction = ValueFunction(value) { text(it.asString) }
    return CSRegistration(
        property1.onChange { valueFunction(text(it, property2.value, property3.value)) },
        property2.onChange { valueFunction(text(property1.value, it, property3.value)) },
        property3.onChange { valueFunction(text(property1.value, property2.value, it)) },
    )
}

fun <T : CSHasDrawable> TextView.drawableStart(property: CSHasChangeValue<T>) =
    property.action { drawable(start = context.drawable(it.drawable)) }

inline fun <T> TextView.drawableStart(
    property: CSHasChangeValue<T>, crossinline getDrawable: (T) -> Int?
) = property.action {
    drawable(start = getDrawable(it)?.let(context::drawable))
}

fun <T : TextView> T.lines(max: Int) = apply { maxLines = max }

fun TextView.hideEndDrawableOnSmallWidth() = apply {
    if (context.isPhone) drawableEnd(null)
}
