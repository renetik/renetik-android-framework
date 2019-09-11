package renetik.android.material.extensions

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import renetik.android.java.extensions.isEmpty
import renetik.android.material.extensions.snackBarWarn
import renetik.android.view.extensions.title

fun List<EditText>.validateNotEmpty(warningMessage: String, onValid: (() -> Unit)? = null): Boolean {
    forEach { editText ->
        val textInputLayout = editText.parent.parent as? TextInputLayout
        if (editText.title.trim().isEmpty) {
            textInputLayout?.let { it.error = warningMessage }
                    ?: let { editText.snackBarWarn("\"${editText.hint}\" $warningMessage") }
            return false
        } else textInputLayout?.error = null
    }
    onValid?.invoke()
    return true
}