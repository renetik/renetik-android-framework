package renetik.android.view.extensions

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.NumberPicker.FOCUS_BEFORE_DESCENDANTS
import android.widget.NumberPicker.FOCUS_BLOCK_DESCENDANTS
import androidx.annotation.ColorInt
import renetik.android.java.extensions.asStringArray
import renetik.android.java.extensions.collections.hasItems
import renetik.android.java.extensions.primitives.count
import renetik.android.java.extensions.setPrivateField2


fun <Row : Any> NumberPicker.loadData(data: List<Row>, selectedIndex: Int) = apply {
    if (data.hasItems) {
        minValue = 1
        displayedValues = data.asStringArray
        maxValue = displayedValues.count
        value = selectedIndex + 1
    }
}

fun NumberPicker.circulate(circulate: Boolean) = apply {
    wrapSelectorWheel = circulate
}

fun NumberPicker.disableTextEditing(disable: Boolean) = apply {
    descendantFocusability = if (disable) FOCUS_BLOCK_DESCENDANTS else FOCUS_BEFORE_DESCENDANTS
}

// setSelectionDividerHeight requires api 29
fun NumberPicker.selectionDividerHeight(height: Int) =
    setPrivateField2("mSelectionDividerHeight", height)

fun NumberPicker.selectionDividerColor(@ColorInt color: Int) =
    setPrivateField2("mSelectionDivider", ColorDrawable(color))

class CSNumberPicker(context: Context, attrs: AttributeSet) : NumberPicker(context, attrs) {

    var editTextList: MutableList<EditText>? = null
    var typeface: Typeface? = null
        set(value) {
            field = value
            for (editText in editTextList!!) value?.let { editText.typeface = it }
        }
    var textSize: Int? = null
        set(value) {
            field = value
            for (editText in editTextList!!) value?.let { editText.textSize = it.toFloat() }
        }

    @ColorInt
    var textColor: Int? = null
        set(value) {
            field = value
            for (editText in editTextList!!) value?.let { editText.setTextColor(it) }
        }

    override fun addView(child: View) {
        super.addView(child)
        updateView(child)
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)
        updateView(child)
    }

    override fun addView(child: View, params: ViewGroup.LayoutParams) {
        super.addView(child, params)
        updateView(child)
    }

    private fun updateView(view: View) {
        if (editTextList == null) editTextList = mutableListOf<EditText>()
        if (view is EditText) editTextList!!.add(view)
    }
}