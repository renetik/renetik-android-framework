package renetik.android.client.okhttp3

import com.androidnetworking.AndroidNetworking.*
import okhttp3.Cache
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import renetik.android.base.application
import renetik.android.client.request.CSRequest
import renetik.android.client.request.CSResponse
import renetik.android.client.request.CSServerData
import renetik.android.java.collections.CSMap
import renetik.android.java.collections.map
import renetik.android.java.common.CSConstants
import renetik.android.java.extensions.primitives.isTrue
import renetik.android.json.data.CSJsonData
import renetik.android.json.data.toJsonObject
import renetik.android.logging.CSLog.logInfo
import java.io.File
import java.util.concurrent.TimeUnit

private class BasicAuthenticatorHeader(val name: String, val username: String, val password: String)
private class Timeouts(val connection: Long, val read: Long, val write: Long)

class CSOkHttpClient(val url: String) {

    private var timeouts: Timeouts? = null

    fun timeouts(connection: Long, read: Long, write: Long) = apply {
        timeouts = Timeouts(connection, read, write)
    }

    private var basicAuthenticatorHeader: BasicAuthenticatorHeader? = null

    fun basicAuthenticatorHeader(name: String, username: String, password: String) {
        basicAuthenticatorHeader = BasicAuthenticatorHeader(name, username, password)
    }

    private var networkInterceptor: Interceptor? = null

    fun enableCachingByOverrideCacheControlToPublic() = apply {
        networkInterceptor = Interceptor { chain ->
            chain.proceed(chain.request()).newBuilder().header("Cache-Control", "public").build()
        }
    }

    val client: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder().cache(Cache(File(application.cacheDir, "ResponseCache"), 10L * CSConstants.MB))
        timeouts?.let {
            builder.connectTimeout(it.connection, TimeUnit.SECONDS)
                    .readTimeout(it.read, TimeUnit.SECONDS).writeTimeout(it.write, TimeUnit.SECONDS)
        }
        basicAuthenticatorHeader?.let {
            builder.authenticator { _, response ->
                response.request().newBuilder().header(it.name, Credentials.basic(it.username, it.password)).build()
            }
        }
        networkInterceptor?.let { builder.addNetworkInterceptor(it).addInterceptor(it) }
        builder.build().apply { initialize(application, this) }
    }


}

fun <ServerDataType : CSServerData> CSOkHttpClient.upload(action: String, file: File, data: ServerDataType) =
        CSResponse("$url/$action", data).also { response ->
            val request = upload(response.url).addMultipartFile("file", file).build()
            logInfo("upload ${request.url} $file")
            request.setUploadProgressListener { uploaded, total -> response.progress = total / uploaded }
                    .getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, response))
        }


fun CSOkHttpClient.get(url: String, params: CSMap<String, String> = map()) = get(null, url, params)

fun CSOkHttpClient.get(request: CSRequest<CSServerData>? = null, url: String, params: CSMap<String, String> = map()) = get(request, url, CSServerData(), params)

fun <ServerDataType : CSServerData> CSOkHttpClient.get(request: CSRequest<*>?, action: String, data: ServerDataType, params: CSMap<String, String> = map()) =
        CSResponse("$url/$action", data).also { response ->
            val builder = get(response.url).addQueryParameter(params)
            if (request?.isforceNetwork.isTrue) builder.responseOnlyFromNetwork
            builder.build().apply {
                logInfo("get $url")
                getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, response))
            }
        }

fun CSOkHttpClient.post(url: String, params: CSMap<String, String> = map()) = post(url, params, CSServerData())

fun <ResponseData : CSServerData> CSOkHttpClient.post(action: String, params: Map<String, String>, responseData: ResponseData) =
        CSResponse("$url/$action", responseData).also { response ->
            val request = post(response.url).addBodyParameter(params).build()
            logInfo("post ${request.url}")
            request.getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, response))
        }

fun CSOkHttpClient.post(url: String, data: CSJsonData) = post(url, data, CSServerData())

fun <ResponseData : CSServerData> CSOkHttpClient.post(url: String, data: CSJsonData, responseData: ResponseData) =
        CSResponse("${this.url}/$url", responseData).also { response ->
            val request = post(response.url).addJSONObjectBody(data.toJsonObject()).build()
            logInfo("post ${request.url}")
            request.getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, response))
        }