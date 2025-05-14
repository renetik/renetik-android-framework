package renetik.android.framework

import android.widget.EditText
import androidx.appcompat.R.drawable.abc_ic_clear_material
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout
import renetik.android.core.kotlin.invoke
import renetik.android.core.kotlin.primitives.ifTrue
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.JobRegistration
import renetik.android.event.registration.launch
import renetik.android.ui.extensions.view.parentView
import renetik.android.ui.extensions.widget.clearText
import renetik.android.ui.extensions.widget.onFocusChange
import kotlin.apply
import kotlin.text.isBlank

val EditText.inputLayout get() = (parentView?.parentView as? TextInputLayout)

fun <T : EditText> T.withStartIcon(icon: Int, onClick: () -> Unit) = apply {
    inputLayout?.setStartIconDrawable(icon)
    inputLayout?.setStartIconOnClickListener { onClick() }
}

fun <T : EditText> T.withEndIcon(icon: Int, onClick: () -> Unit) = apply {
    inputLayout?.setEndIconDrawable(icon)
    inputLayout?.setEndIconOnClickListener { onClick() }
}

fun <T : EditText> T.withStartIconClear(onClear: () -> Unit) = apply {
    fun showClearIcon() = withStartIcon(abc_ic_clear_material, onClear)
    fun removeClearIcon() = inputLayout?.setStartIconDrawable(null)
    fun updateClearIcon() = text.isBlank().ifTrue { removeClearIcon() } ?: showClearIcon()
    updateClearIcon()
    onFocusChange { updateClearIcon() }
    doAfterTextChanged { updateClearIcon() }
}

fun <T : EditText> T.withStartClearText() = withStartIconClear { clearText() }

