package renetik.android.client.request

import renetik.android.framework.lang.CSTimeConstants.Second
import renetik.android.framework.json.data.CSJsonList
import renetik.android.framework.json.data.CSJsonMapStore
import renetik.android.framework.json.data.load
import renetik.android.framework.task.CSDoLaterObject.later

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