package renetik.android.framework.view

import android.content.Context
import android.util.AttributeSet
import com.shawnlin.numberpicker.NumberPicker
import renetik.android.framework.event.CSEvent.Companion.event
import renetik.android.framework.event.property.CSEventPropertyFunctions.property
import renetik.android.framework.logging.CSLog.info

class CSNumberPicker @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NumberPicker(context, attrs, defStyleAttr) {

    val valueProperty = property(value) {
        value = it
    }
    val eventOnScroll = event<Int>()

    init {
        setOnScrollListener { _, scrollState ->
//            isScrolling = scrollState != SCROLL_STATE_IDLE
//            if (!isScrolling) valueProperty.value(value)
            eventOnScroll.fire(scrollState)
        }
        setOnValueChangedListener { _, _, newValue -> valueProperty.value(newValue) }
    }
}