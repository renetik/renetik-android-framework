package renetik.android.network.okhttp3

import com.androidnetworking.error.ANError

fun ANError.infoString() = "Code:${errorCode}, Body:${errorBody}, " +
        "Detail:${errorDetail}, Response:${response}"