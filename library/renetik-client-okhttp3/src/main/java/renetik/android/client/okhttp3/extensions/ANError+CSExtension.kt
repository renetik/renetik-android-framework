package renetik.android.client.okhttp3.extensions

import com.androidnetworking.error.ANError

fun ANError.infoString() = "Code:${errorCode}, Body:${errorBody}, " +
        "Detail:${errorDetail}, Response:${response}"