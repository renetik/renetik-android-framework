package renetik.android.view.extensions

import android.annotation.SuppressLint
import android.view.MotionEvent.ACTION_UP
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import renetik.android.R
import renetik.android.R.drawable.abc_ic_clear_material
import renetik.android.java.event.CSEvent
import renetik.android.java.event.event
import renetik.android.java.event.fire
import renetik.android.java.event.listen
import renetik.android.java.extensions.isNull

val <T : EditText> T.eventClear: CSEvent<Unit>
    get() {
        @Suppress("UNCHECKED_CAST")
        var event = getTag(R.id.EditTextEventOnClearTagKey) as? CSEvent<Unit>
        if (event.isNull) {
            event = event()
            setTag(R.id.EditTextEventOnClearTagKey, event)
        }
        return event!!
    }

fun <T : EditText> T.onClear(listener: () -> Unit): T = apply {
    eventClear.listen(listener)
}

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
        0, 0,
        if (editableText.isNotEmpty()) abc_ic_clear_material else 0, 0
    )
}

val EditText.asTextView: TextView get() = this