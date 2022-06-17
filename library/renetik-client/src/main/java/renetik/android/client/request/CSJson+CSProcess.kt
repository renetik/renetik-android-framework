package renetik.android.client.request

import renetik.android.core.lang.CSTimeConstants.Second
import renetik.android.json.CSJsonArray
import renetik.android.framework.store.json.CSStoreJsonObject
import renetik.android.json.load

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

fun <T : renetik.android.json.CSJsonArray> T.mockProcessSuccess(data: String) = CSProcessBase<T>().apply {
    later(1 * Second) {
        success(load(data))
    }
}