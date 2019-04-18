package renetik.android.client.okhttp3

import com.androidnetworking.AndroidNetworking.*
import okhttp3.Cache
import okhttp3.Credentials.basic
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import renetik.android.base.application
import renetik.android.client.request.CSOperation
import renetik.android.client.request.CSProcess
import renetik.android.client.request.CSServerData
import renetik.android.java.common.CSConstants.MB
import renetik.android.java.extensions.collections.map
import renetik.android.java.extensions.primitives.isTrue
import renetik.android.json.data.CSJsonData
import renetik.android.json.data.toJsonObject
import renetik.android.logging.CSLog.logInfo
import java.io.File
import java.util.concurrent.TimeUnit.SECONDS

private class BasicAuthHeader(val name: String, val username: String, val password: String)
private class Timeouts(val connection: Long, val read: Long, val write: Long)

class CSOkHttpClient(val url: String) {

    private var timeouts: Timeouts? = null

    fun timeouts(connection: Long, read: Long, write: Long) = apply {
        timeouts = Timeouts(connection, read, write)
    }

    private var basicAuthHeader: BasicAuthHeader? = null

    fun basicAuthenticatorHeader(name: String, username: String, password: String) {
        basicAuthHeader = BasicAuthHeader(name, username, password)
    }

    private var networkInterceptor: Interceptor? = null

    fun enableCachingByOverrideCacheControlToPublic() = apply {
        networkInterceptor = Interceptor { chain ->
            chain.proceed(chain.request()).newBuilder().header("Cache-Control", "public").build()
        }
    }

    val client: OkHttpClient by lazy {
        val builder = Builder().cache(Cache(File(application.cacheDir, "ResponseCache"), 10L * MB))
        timeouts?.let {
            builder.connectTimeout(it.connection, SECONDS)
                    .readTimeout(it.read, SECONDS).writeTimeout(it.write, SECONDS)
        }
        basicAuthHeader?.let {
            builder.authenticator { _, response ->
                response.request().newBuilder().header(it.name, basic(it.username, it.password)).build()
            }
        }
        networkInterceptor?.let { builder.addNetworkInterceptor(it).addInterceptor(it) }
        builder.build().apply { initialize(application, this) }
    }


}

fun <ServerDataType : CSServerData> CSOkHttpClient.upload(
        action: String, file: File, data: ServerDataType) =
        CSProcess("$url/$action", data).also { response ->
            val request = upload(response.url).addMultipartFile("file", file).build()
            logInfo("upload ${request.url} $file")
            request.setUploadProgressListener { uploaded, total ->
                response.progress = total / uploaded
            }.getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, response))
        }


fun CSOkHttpClient.get(url: String, params: Map<String, String> = map()) = get(null, url, params)

fun CSOkHttpClient.get(operation: CSOperation<CSServerData>? = null, url: String,
                       params: Map<String, String> = map()) =
        get(operation, url, CSServerData(), params)

fun <ServerDataType : CSServerData> CSOkHttpClient.get(
        operation: CSOperation<*>?, action: String, data: ServerDataType,
        params: Map<String, String> = map()) =
        CSProcess("$url/$action", data).also { response ->
            val builder = get(response.url).addQueryParameter(params)
            if (operation?.isForceNetwork.isTrue) builder.responseOnlyFromNetwork
            builder.build().apply {
                logInfo("get $url")
                getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, response))
            }
        }

fun CSOkHttpClient.post(url: String, params: Map<String, String> = map()) =
        post(url, params, CSServerData())

fun <ResponseData : CSServerData> CSOkHttpClient.post(
        action: String, params: Map<String, String>, responseData: ResponseData) =
        CSProcess("$url/$action", responseData).also { response ->
            val request = post(response.url).addBodyParameter(params).build()
            logInfo("post ${request.url}")
            request.getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, response))
        }

fun CSOkHttpClient.post(url: String, data: CSJsonData) = post(url, data, CSServerData())

fun <ResponseData : CSServerData> CSOkHttpClient.post(
        url: String, data: CSJsonData, responseData: ResponseData) =
        CSProcess("${this.url}/$url", responseData).also { response ->
            val request = post(response.url).addJSONObjectBody(data.toJsonObject()).build()
            logInfo("post ${request.url}")
            request.getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, response))
        }