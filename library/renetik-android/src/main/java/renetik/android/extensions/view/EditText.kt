package renetik.android.extensions.view

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent.ACTION_UP
import android.widget.EditText
import renetik.android.R.drawable.abc_ic_clear_material

@SuppressLint("PrivateResource")
fun <T : EditText> T.withClear(): T {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable) {
            setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    if (editable.isNotEmpty()) abc_ic_clear_material else 0, 0)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
    })

    setOnTouchListener { _, event ->
        if (event.action == ACTION_UP && event.x >= (right - this.compoundPaddingRight)) {
            setText("")
            return@setOnTouchListener true
        }
        false
    }
    return this
}