package renetik.android.network

import renetik.android.core.kotlin.primitives.second
import renetik.android.event.process.CSProcess
import renetik.android.event.util.CSLater.later
import renetik.android.json.array.CSJsonArray
import renetik.android.json.array.load
import renetik.android.json.obj.CSJsonObject
import renetik.android.json.obj.load

fun <T : CSJsonObject> T.mockProcessSuccess(data: String) =
    CSProcess<T>().apply {
        later(1.second) { success(load(data)) }
    }

fun <T : CSJsonObject> T.mockProcessSuccess() =
    CSProcess<T>().apply {
        later(1.second) { success(this@mockProcessSuccess) }
    }

fun <T : CSJsonArray> T.mockProcessSuccess(data: String) =
    CSProcess<T>().apply {
        later(1.second) { success(load(data)) }
    }