package renetik.android.client.okhttp3

import com.androidnetworking.AndroidNetworking
import okhttp3.*
import okhttp3.Credentials.basic
import okhttp3.OkHttpClient.Builder
import renetik.android.base.CSApplicationObject.application
import renetik.android.java.common.CSDataConstants.MB
import renetik.android.java.event.event
import renetik.android.java.event.listen
import java.io.File
import java.net.CookieManager
import java.util.concurrent.TimeUnit.SECONDS
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory

private class BasicAuthHeader(val name: String, val username: String, val password: String)
private class Timeouts(val connection: Long, val read: Long, val write: Long)

class CSOkHttpClient(val url: String) {

    var hostNameVerifier: HostnameVerifier? = null
    var sslSocketFactory: SSLSocketFactory? = null
    private var timeouts: Timeouts? = null
    val eventCookiesReceived = event<List<Cookie>>()
    val cookies: MutableList<Cookie> = mutableListOf()
    private var cookieJar: CookieJar = object : CookieJar {
        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) =
            eventCookiesReceived.fire(cookies)

        override fun loadForRequest(url: HttpUrl): List<Cookie>? = cookies
    }

    fun onCookiesReceived(function: (List<Cookie>) -> Unit) =
        eventCookiesReceived.listen(function)

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
        val cookieManager = CookieManager()
        builder.cookieJar(JavaNetCookieJar(cookieManager))
        timeouts?.let {
            builder.connectTimeout(it.connection, SECONDS)
                .readTimeout(it.read, SECONDS).writeTimeout(it.write, SECONDS)
        }
        basicAuthHeader?.let {
            builder.authenticator { _, response ->
                response.request().newBuilder().header(it.name, basic(it.username, it.password))
                    .build()
            }
        }
        networkInterceptor?.let { builder.addNetworkInterceptor(it).addInterceptor(it) }
        sslSocketFactory?.let { builder.sslSocketFactory(it) }
        hostNameVerifier?.let { builder.hostnameVerifier(it) }
        builder.build().apply { AndroidNetworking.initialize(application, this) }
    }
}