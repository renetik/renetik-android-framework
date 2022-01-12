package renetik.android.content

import android.widget.Toast.*
import renetik.android.content.CSToastLength.ShortTime
import renetik.android.framework.CSApplication.Companion.application

object CSToast {
    fun toast(text: String) = makeText(application, text, LENGTH_LONG).show()
    fun toast(text: String, time: CSToastLength = ShortTime) =
        makeText(application, text, time.value).show()
}

enum class CSToastLength(val value: Int) {
    LongTime(LENGTH_LONG), ShortTime(LENGTH_SHORT)
}