package renetik.android.client.request

import renetik.android.core.lang.CSTimeConstants.Second
import renetik.android.framework.json.CSJsonArray
import renetik.android.framework.store.json.CSStoreJsonObject
import renetik.android.framework.json.load

fun <T : CSStoreJsonObject> T.mockProcessSuccess(data: String) = CSProcessBase<T>().apply {
    later(1 * Second) {
        success(load(data))
    }
}

fun <T : CSStoreJsonObject> T.mockProcessSuccess() = CSProcessBase<T>().apply {
    later(1 * Second) {
        success(this@mockProcessSuccess)
    }
}

fun <T : CSJsonArray> T.mockProcessSuccess(data: String) = CSProcessBase<T>().apply {
    later(1 * Second) {
        success(load(data))
    }
}