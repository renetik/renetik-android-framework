package renetik.android.material.extensions

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputLayout.END_ICON_CUSTOM
import renetik.android.R
import renetik.android.base.CSView
import renetik.android.java.event.event
import renetik.android.java.event.fire
import renetik.android.java.event.listen
import renetik.android.view.extensions.*

fun CSView<*>.textInputLayout(id: Int,
                              onClick: ((TextInputLayout) -> Unit)? = null): TextInputLayout {
    val inputLayout = view.findView<TextInputLayout>(id)!!
    onClick?.let { inputLayout.editText?.onClick { onClick(inputLayout) } }
    return inputLayout
}

val <T : TextInputLayout> T.eventClear get() = tagProperty(R.id.EditTextEventOnClearTagKey) { event<Unit>() }

fun <T : TextInputLayout> T.onClear(listener: () -> Unit): T = apply {
    eventClear.listen(listener)
}

@SuppressLint("PrivateResource")
fun <T : TextInputLayout> T.withClear(): TextInputLayout = apply {
    setEndIconDrawable(R.drawable.abc_ic_clear_material)
    endIconMode = END_ICON_CUSTOM
    updateClearIcon()
    editText!!.doAfterTextChanged { updateClearIcon() }
    setEndIconOnClickListener {
        title = ""
        eventClear.fire()
    }
}

fun <T : TextInputLayout> T.updateClearIcon() {
    isEndIconVisible = title.isNotEmpty()
}

fun <T : TextInputLayout> T.clearError() = apply {
    error = null
    isErrorEnabled = false
}

var <T : TextInputLayout> T.title: String
    get() = editText!!.title
    set(value) {
        editText!!.title = value
    }

fun <T : TextInputLayout> T.title(string: String) = apply { title = string }

fun <T : TextInputLayout> T.onChange(onChange: (view: T) -> Unit) =
    apply { editText!!.onChange { onChange(this) } }