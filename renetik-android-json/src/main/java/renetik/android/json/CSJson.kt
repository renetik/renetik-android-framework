package renetik.android.json

import android.annotation.SuppressLint
import renetik.android.core.kotlin.setPrivateField
import renetik.android.core.lang.catchWarn

object CSJson {
    var forceString = false
    var isJsonPretty = false

    @SuppressLint("DiscouragedPrivateApi")
    fun JSONObject() = org.json.JSONObject().apply {
        catchWarn<ReflectiveOperationException> {
            setPrivateField("nameValuePairs", LinkedHashMap<String, Any?>())
        }
    }

    fun forceString() = apply { forceString = true }
}