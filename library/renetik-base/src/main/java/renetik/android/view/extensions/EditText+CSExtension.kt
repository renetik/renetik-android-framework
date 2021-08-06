package renetik.android.view.extensions

import android.annotation.SuppressLint
import android.view.KeyEvent.ACTION_UP
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import renetik.android.R
import renetik.android.framework.event.*
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.java.extensions.asString
import renetik.android.primitives.isEmpty

val EditText.eventClear get() = propertyWithTag(R.id.ViewEventOnClearTag) { event<Unit>() }

fun <T : EditText> T.onClear(listener: () -> Unit): T = apply { eventClear.listen(listener) }

@SuppressLint("ClickableViewAccessibility")
fun <T : EditText> T.withClear(): T {
    updateClearIcon()
    doAfterTextChanged { updateClearIcon() }
    setOnTouchListener { _, event ->
        if (event.action == ACTION_UP && event.x >= (right - this.compoundPaddingRight)) {
            setText("")
            eventClear.fire()
            return@setOnTouchListener true
        }
        false
    }
    return this
}

@SuppressLint("PrivateResource")
fun EditText.updateClearIcon() {
    setCompoundDrawablesWithIntrinsicBounds(
        0, 0, if (editableText.isNotEmpty()) R.drawable.abc_ic_clear_material else 0, 0
    )
}

val EditText.asTextView: TextView get() = this

fun EditText.text(parent: CSVisibleEventOwner, property: CSEventProperty<String?>) = apply {
    fun updateText() = text(property.value.asString)
    val onPropertyChange = parent.whileShowing(property.onChange { updateText() })
    onTextChange {
        onPropertyChange.pause()
        property.value = if (it.text().isEmpty) null else it.text()
        onPropertyChange.resume()
    }
    updateText()
}