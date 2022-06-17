package renetik.android.client.request

import renetik.android.core.lang.CSTimeConstants.Second
import renetik.android.framework.json.CSJsonArray
import renetik.android.framework.json.CSJsonObject
import renetik.android.framework.json.load

fun <T : CSJsonObject> T.mockProcessSuccess(data: String) = CSProcessBase<T>().apply {
    later(1 * Second) {
        success(load(data))
    }
}

fun <T : CSJsonObject> T.mockProcessSuccess() = CSProcessBase<T>().apply {
    later(1 * Second) {
        success(this@mockProcessSuccess)
    }
}

fun <T : CSJsonArray> T.mockProcessSuccess(data: String) = CSProcessBase<T>().apply {
    later(1 * Second) {
        success(load(data))
    }
}