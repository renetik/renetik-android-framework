package renetik.android.client.okhttp3

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import okhttp3.Cache
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import renetik.android.base.application
import renetik.android.client.request.CSResponse
import renetik.android.client.request.CSServerData
import renetik.android.java.collections.CSMap
import renetik.android.java.collections.map
import renetik.android.java.common.CSConstants
import renetik.android.logging.CSLog
import java.io.File
import java.util.concurrent.TimeUnit

private class BasicAuthenticatorHeader(val name: String, val username: String, val password: String)
private class Timeouts(val connection: Long, val read: Long, val write: Long)

class CSOkHttpClient {

    private var timeouts: Timeouts? = null

    fun timeouts(connection: Long, read: Long, write: Long) {
        timeouts = Timeouts(connection, read, write)
    }

    private var basicAuthenticatorHeader: BasicAuthenticatorHeader? = null

    fun basicAuthenticatorHeader(name: String, username: String, password: String) {
        basicAuthenticatorHeader = BasicAuthenticatorHeader(name, username, password)
    }

    private var networkInterceptor: Interceptor? = null

    fun enableCachingByOverrideCacheControlToPublic() {
        networkInterceptor = Interceptor { chain ->
            chain.proceed(chain.request()).newBuilder().header("Cache-Control", "public").build()
        }
    }

    fun <ServerDataType : CSServerData> upload(url: String, file: File, data: ServerDataType) =
            CSResponse(url, data).also { response ->
                val request = AndroidNetworking.upload(response.url).addMultipartFile("file", file).setPriority(Priority.HIGH).build()
                CSLog.logInfo("upload ${request.url} $file")
                request.setUploadProgressListener { uploaded, total -> response.progress = total / uploaded }
                        .getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, response))
            }

    fun <ServerDataType : CSServerData> get(url: String, data: ServerDataType, params: CSMap<String, String> = map()) =
            CSResponse(url, data).also { response ->
                val request = AndroidNetworking.get(response.url).setPriority(Priority.HIGH).addQueryParameter(params).build()
                CSLog.logInfo("get ${request.url}")
                request.getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, response))
            }


    private val client: OkHttpClient by lazy {
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
        builder.build().apply { AndroidNetworking.initialize(application, this) }
    }
}