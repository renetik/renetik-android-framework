package renetik.android.network.process

import renetik.android.core.lang.CSTimeConstants.Second
import renetik.android.json.obj.CSJsonObject
import renetik.android.json.obj.load
import renetik.android.json.array.CSJsonArray
import renetik.android.json.array.load

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