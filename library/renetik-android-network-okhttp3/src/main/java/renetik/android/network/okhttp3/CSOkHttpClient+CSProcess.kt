package renetik.android.network.okhttp3

import com.androidnetworking.AndroidNetworking
import org.json.JSONObject
import org.json.JSONTokener
import renetik.android.core.extensions.content.isNetworkConnected
import renetik.android.core.kotlin.isNotNull
import renetik.android.core.kotlin.primitives.isFalse
import renetik.android.core.kotlin.primitives.isTrue
import renetik.android.core.lang.CSEnvironment.app
import renetik.android.core.lang.CSTimeConstants.Minute
import renetik.android.core.logging.CSLog.logInfo
import renetik.android.event.common.CSHasDestruct
import renetik.android.json.obj.CSJsonObject
import renetik.android.json.obj.toJsonObject
import renetik.android.json.toJSONArray
import renetik.android.json.toJSONObject
import renetik.android.json.toJson
import renetik.android.network.data.CSHttpResponseData
import renetik.android.network.operation.CSHttpOperation
import renetik.android.network.CSHttpProcess
import java.io.File
import java.util.concurrent.TimeUnit

fun <ServerDataType : CSHttpResponseData> CSOkHttpClient.upload(
    parent: CSHasDestruct,
    service: String,
    file: File,
    data: ServerDataType
) = CSHttpProcess(parent, "$url/$service", data).also { process ->
    val request = AndroidNetworking.upload(process.url).addMultipartFile("file", file).build()
    logInfo { "upload ${request.url} $file" }
    request.setUploadProgressListener { uploaded, total ->
        process.progress = total / uploaded
    }.getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, process))
}

fun <ServerDataType : CSHttpResponseData> CSOkHttpClient.get(
    operation: CSHttpOperation<*>, service: String, data: ServerDataType,
    params: Map<String, String> = emptyMap()
) = CSHttpProcess(operation, "$url/$service", data).also { process ->
    val builder = AndroidNetworking.get(process.url!!).addQueryParameter(params)

    if (operation.isCached.isFalse) builder.doNotCacheResponse()
    operation.expireMinutes.isNotNull {
        builder.setMaxStaleCacheControl(it * Minute, TimeUnit.MILLISECONDS)
    }
    if (operation.isRefresh.isTrue) builder.responseOnlyFromNetwork
    else if (!app.isNetworkConnected && operation.isCached.isTrue
        || operation.isJustUseCache.isTrue
    ) {
        builder.responseOnlyIfCached
    }

    builder.build().apply {
        logInfo { "get $url" }
        getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, process))
    }
}

fun <ResponseData : CSHttpResponseData> CSOkHttpClient.post(
    parent: CSHasDestruct, service: String,
    responseData: ResponseData, params: Map<String, String>
) = CSHttpProcess(parent, "$url/$service", responseData).also { process ->
    val request = AndroidNetworking.post(process.url).addBodyParameter(params).build()
    logInfo { "post ${request.url}" }
    request.getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, process))
}

fun <ResponseData : CSHttpResponseData> CSOkHttpClient.postJson(
    parent: CSHasDestruct, service: String, responseData: ResponseData, data: Map<String, *>
) = post(parent, service, responseData, data.toJSONObject())

fun <ResponseData : CSHttpResponseData> CSOkHttpClient.postJsonObject(
    parent: CSHasDestruct, service: String,
    responseData: ResponseData, data: String
) = post(parent, service, responseData, JSONTokener(data).nextValue() as JSONObject)

fun <ResponseData : CSHttpResponseData> CSOkHttpClient.post(
    parent: CSHasDestruct, service: String,
    responseData: ResponseData, data: JSONObject
) = CSHttpProcess(parent, "$url/$service", responseData).also { process ->
    val request = AndroidNetworking.post(process.url).addJSONObjectBody(data).build()
    logInfo { "post:${request.url} json:${data.toJson(formatted = true)}" }
    request.getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, process))
}

fun <ResponseData : CSHttpResponseData> CSOkHttpClient.postJson(
    parent: CSHasDestruct, service: String, responseData: ResponseData, data: List<*>
) = CSHttpProcess(parent, "$url/$service", responseData).also { process ->
    val request = AndroidNetworking.post(process.url).addJSONArrayBody(data.toJSONArray()).build()
    logInfo { "post:${request.url} json:${data.toJson(formatted = true)}" }
    request.getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, process))
}

fun <ResponseData : CSHttpResponseData> CSOkHttpClient.post(
    parent: CSHasDestruct, url: String, data: CSJsonObject, responseData: ResponseData
) = CSHttpProcess(parent, "${this.url}/$url", responseData).also { process ->
    val request = AndroidNetworking.post(process.url).addJSONObjectBody(data.toJsonObject()).build()
    logInfo { "post ${request.url}" }
    request.getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, process))
}