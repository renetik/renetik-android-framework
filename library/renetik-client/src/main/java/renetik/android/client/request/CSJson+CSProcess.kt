package renetik.android.client.request

import renetik.android.java.common.CSTimeConstants.Second
import renetik.android.json.data.CSJsonList
import renetik.android.json.data.CSJsonMap
import renetik.android.json.data.load
import renetik.android.task.CSDoLaterObject.later

fun <T : CSJsonMap> T.mockSuccess(data: String) = CSProcess<T>().apply {
    later(1 * Second) {
        success(load(data))
    }
}

fun <T : CSJsonList> T.mockSuccess(data: String) = CSProcess<T>().apply {
    later(1 * Second) {
        success(load(data))
    }
}