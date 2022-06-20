package renetik.android.network.process

import renetik.android.core.lang.CSTimeConstants.Second
import renetik.android.json.CSJsonArray
import renetik.android.json.CSJsonObject
import renetik.android.json.load

fun <T : CSJsonObject> T.mockProcessSuccess(data: String) =
    CSProcessBase<T>().apply {
        later(1 * Second) { success(load(data)) }
    }

fun <T : CSJsonObject> T.mockProcessSuccess() =
    CSProcessBase<T>().apply {
        later(1 * Second) { success(this@mockProcessSuccess) }
    }

fun <T : CSJsonArray> T.mockProcessSuccess(data: String) =
    CSProcessBase<T>().apply {
        later(1 * Second) { success(load(data)) }
    }