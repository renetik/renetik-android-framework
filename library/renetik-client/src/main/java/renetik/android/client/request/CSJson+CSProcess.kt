package renetik.android.client.request

import renetik.android.framework.lang.CSTimeConstants.Second
import renetik.android.json.data.CSJsonList
import renetik.android.json.data.CSJsonMapStore
import renetik.android.json.data.load
import renetik.android.task.CSDoLaterObject.later

fun <T : CSJsonMapStore> T.mockProcessSuccess(data: String) = CSProcess<T>().apply {
    later(1 * Second) {
        success(load(data))
    }
}

fun <T : CSJsonMapStore> T.mockProcessSuccess() = CSProcess<T>().apply {
    later(1 * Second) {
        success(this@mockProcessSuccess)
    }
}

fun <T : CSJsonList> T.mockProcessSuccess(data: String) = CSProcess<T>().apply {
    later(1 * Second) {
        success(load(data))
    }
}