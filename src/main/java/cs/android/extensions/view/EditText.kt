package cs.android.extensions.view

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent.ACTION_UP
import android.view.View.OnTouchListener
import android.widget.EditText
import cs.android.R.drawable.abc_ic_clear_material

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

    setOnTouchListener(OnTouchListener { _, event ->
        if (event.action == ACTION_UP) {
            if (event.rawX >= (this.right - this.compoundPaddingRight)) {
                this.setText("")
                return@OnTouchListener true
            }
        }
        return@OnTouchListener false
    })
    return this
}