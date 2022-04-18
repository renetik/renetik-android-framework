package renetik.android.content

import android.widget.Toast.*
import renetik.android.content.CSToastLength.LongTime
import renetik.android.content.CSToastLength.ShortTime
import renetik.android.framework.CSApplication.Companion.app

object CSToast {
    fun toast(text: String) = toast(text, LongTime)

    fun toast(text: String, time: CSToastLength = ShortTime) =
        makeText(app, text, time.value).show()
}

enum class CSToastLength(val value: Int) {
    LongTime(LENGTH_LONG), ShortTime(LENGTH_SHORT)
}