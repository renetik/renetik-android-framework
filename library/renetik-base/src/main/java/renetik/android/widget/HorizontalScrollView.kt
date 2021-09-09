package renetik.android.widget

import android.os.Build
import android.widget.HorizontalScrollView
import androidx.annotation.RequiresApi
import renetik.android.R
import renetik.android.framework.event.event
import renetik.android.framework.event.listen
import renetik.android.view.propertyWithTag

fun HorizontalScrollView.onChange(function: (scrollX: Int) -> Unit) =
    eventChange.listen(function)

val HorizontalScrollView.eventChange
    @RequiresApi(Build.VERSION_CODES.M)
    get() = propertyWithTag(R.id.ViewEventOnChangeTag) {
        event<Int>().apply {
            setOnScrollChangeListener { _, scrollX, _, _, _ ->
                fire(scrollX)
            }
        }
    }

val HorizontalScrollView.content get() = getChildAt(0)