package renetik.android.ui.extensions.widget

import android.annotation.SuppressLint
import android.view.MotionEvent.ACTION_UP
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import renetik.android.core.kotlin.asString
import renetik.android.core.kotlin.primitives.isEmpty
import renetik.android.core.lang.ArgFunc
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.paused
import renetik.android.ui.R
import renetik.android.ui.extensions.view.propertyWithTag

val EditText.eventClear get() = propertyWithTag(R.id.ViewEventOnClearTag) { event<EditText>() }

fun <T : EditText> T.onClear(listener: (EditText) -> Unit): T =
    apply { eventClear.listen(listener) }

//all compoundDrawables needs to be set programmatically
@SuppressLint("ClickableViewAccessibility")
fun <T : EditText> T.withClear(
    showOnFocus: Boolean = false, onClear: ArgFunc<EditText>? = null
) = apply {
    fun updateClearIcon() {
        endDrawable(
            if (editableText.isNotEmpty() || (showOnFocus && isFocused))
                androidx.appcompat.R.drawable.abc_ic_clear_material else null
        )
    }
    updateClearIcon()
    if (showOnFocus) onFocusChange { updateClearIcon() }
    doAfterTextChanged { updateClearIcon() }
    setOnTouchListener { _, event ->
        if (event.action == ACTION_UP && event.x >= (width - this.compoundPaddingRight)) {
            setText("")
            eventClear.fire(this)
            return@setOnTouchListener true
        }
        false
    }
    onClear?.let { onClear(it) }
}

fun EditText.textProperty(property: CSProperty<String>): CSRegistration {
    fun updateText() = text(property.value.asString)
    val propertyOnChange = property.onChange { updateText() }
    onTextChange {
        propertyOnChange.paused { property.value = if (text().isEmpty) "" else text() }
    }
    updateText()
    return propertyOnChange
}

fun EditText.withKeyboardDone(function: () -> Unit) = apply {
    imeOptions = EditorInfo.IME_ACTION_DONE
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            function(); true
        } else false
    }
}