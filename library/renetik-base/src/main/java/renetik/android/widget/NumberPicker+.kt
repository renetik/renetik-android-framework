package renetik.android.widget

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.text.InputType.TYPE_CLASS_NUMBER
import android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL
import android.util.AttributeSet
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.NumberPicker.FOCUS_BEFORE_DESCENDANTS
import android.widget.NumberPicker.FOCUS_BLOCK_DESCENDANTS
import androidx.annotation.ColorInt
import renetik.android.framework.Func
import renetik.android.framework.event.CSEventRegistration
import renetik.android.framework.event.pause
import renetik.android.framework.event.property.CSEventProperty
import renetik.kotlin.asStringArray
import renetik.kotlin.collections.hasItems
import renetik.kotlin.collections.index
import renetik.kotlin.privateField
import renetik.kotlin.setPrivateField2


fun <Row : Any> NumberPicker.loadData(data: List<Row>, selected: Row? = null) =
    loadData(data, selectedIndex = data.index(selected) ?: 0)

fun <Row : Any> NumberPicker.loadData(data: List<Row>, selectedIndex: Int = 0) = apply {
    if (data.hasItems) {
        minValue = 0
        value = 0
        val newMaxValue = data.size - 1
        if (newMaxValue > maxValue) {
            displayedValues = data.asStringArray
            maxValue = newMaxValue
        } else {
            maxValue = newMaxValue
            displayedValues = data.asStringArray
        }
        value = selectedIndex
    }
}

fun NumberPicker.onValueChange(function: Func) = apply {
    setOnValueChangedListener { _, _, _ -> function() }
}

fun NumberPicker.circulate(circulate: Boolean) = apply {
    wrapSelectorWheel = circulate
}

fun NumberPicker.disableTextEditing(disable: Boolean) = apply {
    descendantFocusability = if (disable) FOCUS_BLOCK_DESCENDANTS else FOCUS_BEFORE_DESCENDANTS
}

fun NumberPicker.min(value: Int) = apply { minValue = value }

fun NumberPicker.max(value: Int) = apply { maxValue = value }

@JvmName("valuePropertyInt")
fun NumberPicker.value(property: CSEventProperty<Int>): CSEventRegistration {
    val registration = value(property) { it }
    onValueChange { registration.pause().use { property.value = value } }
    return registration
}

fun <T> NumberPicker.value(property: CSEventProperty<T>, function: (T) -> Int)
        : CSEventRegistration {
    value = function(property.value)
    return property.onChange { value = function(property.value) }
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

    private fun updateTextAttributes() {
        disableFocusability()
        textColor?.let { selectorWheelPaint?.color = it }
        textSize?.let { selectorWheelPaint?.textSize = it.toFloat() }
        typeface?.let { selectorWheelPaint?.typeface = it }

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is EditText) {
                textColor?.let { child.setTextColor(it) }
                textSize?.let { child.setTextSize(COMPLEX_UNIT_SP, it.toFloat()) }
                child.inputType = TYPE_CLASS_NUMBER or TYPE_NUMBER_VARIATION_NORMAL
                typeface?.let { child.typeface = it }
                invalidate()
            }
        }
    }

    private fun disableFocusability() {
        inputText?.filters = arrayOfNulls(0)
    }

    private val selectorWheelPaint: Paint? by lazy {
        privateField<Paint>("mSelectorWheelPaint")
    }

    private val inputText: EditText? by lazy {
        privateField<EditText>("mInputText")
    }
}