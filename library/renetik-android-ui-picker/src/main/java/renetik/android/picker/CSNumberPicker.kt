package renetik.android.picker

import android.content.Context
import android.util.AttributeSet
import com.shawnlin.numberpicker.NumberPicker
import com.shawnlin.numberpicker.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.property.CSEventPropertyFunctions.property

class CSNumberPicker @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NumberPicker(context, attrs, defStyleAttr) {

    val valueProperty = property(value) { value = it }
    val eventOnScroll = event<Int>()
    private var isScrolling: Boolean = false

    init {
        setOnScrollListener { _, scrollState ->
            isScrolling = scrollState != SCROLL_STATE_IDLE
            if (!isScrolling) valueProperty.value(value)
            eventOnScroll.fire(scrollState)
        }
        setOnValueChangedListener { _, _, newValue ->
            if (!isScrolling) valueProperty.value(newValue)
        }
    }
}