package renetik.android.core.extensions.content

import android.widget.Toast.*
import renetik.android.core.CSApplication.Companion.app
import renetik.android.core.extensions.content.CSToastLength.LongTime
import renetik.android.core.extensions.content.CSToastLength.ShortTime

object CSToast {
    fun toast(text: String) = toast(text, LongTime)

    fun toast(text: String, time: CSToastLength = ShortTime) =
        makeText(app, text, time.value).show()
}

enum class CSToastLength(val value: Int) {
    LongTime(LENGTH_LONG), ShortTime(LENGTH_SHORT)
}