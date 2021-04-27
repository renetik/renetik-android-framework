package renetik.android.client.request

import renetik.android.framework.lang.CSTimeConstants.Second
import renetik.android.json.data.CSJsonList
import renetik.android.json.data.CSJsonMap
import renetik.android.json.data.load
import renetik.android.task.CSDoLaterObject.later

fun <T : CSJsonMap> T.mockProcessSuccess(data: String) = CSProcess<T>().apply {
    later(1 * Second) {
        success(load(data))
    }
}

fun <T : CSJsonMap> T.mockProcessSuccess() = CSProcess<T>().apply {
    later(1 * Second) {
        success(this@mockProcessSuccess)
    }
}

fun <T : CSJsonList> T.mockProcessSuccess(data: String) = CSProcess<T>().apply {
    later(1 * Second) {
        success(load(data))
    }
}