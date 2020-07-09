package renetik.android.material.extensions

import android.view.View
import android.view.ViewParent
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import renetik.android.java.extensions.isEmpty
import renetik.android.view.extensions.title

fun List<EditText>.validateNotEmpty(warningMessage: String,
                                    onValid: (() -> Unit)? = null): Boolean {
    forEach { editText ->
        val textInputLayout = editText.textInputLayout
        if (editText.title.trim().isEmpty) {
            textInputLayout?.let { it.error = warningMessage }
                ?: let { editText.snackBarWarn("\"${editText.hint}\" $warningMessage") }
            return false
        } else textInputLayout?.error = null
    }
    onValid?.invoke()
    return true
}

val EditText.textInputLayout: TextInputLayout?
    get() {
        var parent: ViewParent = parent
        while (parent is View) {
            if (parent is TextInputLayout) return parent
            parent = parent.getParent()
        }
        return null
    }
