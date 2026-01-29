package renetik.android.ui.extensions.widget

import android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import renetik.android.core.kotlin.asString
import renetik.android.core.kotlin.primitives.isBlank
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.action
import renetik.android.event.registration.paused

fun <T : EditText> T.multiline() = apply {
    inputType = inputType or TYPE_TEXT_FLAG_MULTI_LINE
    minLines = 0; maxLines = Int.MAX_VALUE; isSingleLine = false
}

fun TextView.property(property: CSProperty<String>): CSRegistration =
    property(property) { it.asString }

fun TextView.property(property: CSProperty<String>, toString: (String) -> String): CSRegistration {
    val propertyOnChange = property.action { text(toString(property.value)) }
    return CSRegistration(propertyOnChange, onTextChange {
        propertyOnChange.paused { property.value = if (text().isBlank) "" else text() }
    })
}

@JvmName("propertyNullable")
fun EditText.property(property: CSProperty<String?>): CSRegistration {
    val propertyOnChange = property.action { text(property.value.asString) }
    return CSRegistration(propertyOnChange, onTextChange {
        propertyOnChange.paused { property.value = if (text().isBlank) "" else text() }
    })
}

fun EditText.withKeyboardDone(function: () -> Unit) = apply {
    imeOptions = EditorInfo.IME_ACTION_DONE
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            function(); true
        } else false
    }
}

fun EditText.cursorToEnd() = apply { setSelection(text.length) }