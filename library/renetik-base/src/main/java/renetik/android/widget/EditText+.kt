package renetik.android.widget

import android.annotation.SuppressLint
import android.view.MotionEvent.ACTION_UP
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import renetik.android.R
import renetik.android.framework.event.*
import renetik.android.framework.event.CSEvent.Companion.event
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.primitives.isEmpty
import renetik.android.view.propertyWithTag
import renetik.kotlin.asString

val EditText.eventClear get() = propertyWithTag(R.id.ViewEventOnClearTag) { event<EditText>() }

fun <T : EditText> T.onClear(listener: (EditText) -> Unit): T =
    apply { eventClear.listen(listener) }

@SuppressLint("ClickableViewAccessibility")
fun <T : EditText> T.withClear(showOnFocus: Boolean = false) = apply {
    fun updateClearIcon() {
        endDrawable(if (editableText.isNotEmpty() || (showOnFocus && isFocused))
            androidx.appcompat.R.drawable.abc_ic_clear_material else null)
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
}

fun EditText.textProperty(property: CSEventProperty<String>): CSEventRegistration {
    fun updateText() = text(property.value.asString)
    val propertyOnChange = property.onChange { updateText() }
    onTextChange {
        propertyOnChange.pause().use { property.value = if (text().isEmpty) "" else text() }
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