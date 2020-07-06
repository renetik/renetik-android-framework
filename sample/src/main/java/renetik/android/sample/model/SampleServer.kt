package renetik.android.sample.model

import renetik.android.client.okhttp3.CSOkHttpClient
import renetik.android.client.okhttp3.get
import renetik.android.client.okhttp3.post
import renetik.android.client.request.*
import renetik.android.json.data.extensions.getString
import renetik.android.json.data.properties.CSJsonString

const val SERVER_URL = "https://renetik-library-sample-server.herokuapp.com/api"
const val SERVER_DEV_URL = "http://localhost:8080/api"
const val USERNAME = "username"
const val PASSWORD = "password"

class SampleServer : CSServerWithPing {

    private val client = CSOkHttpClient(SERVER_URL)
        .apply { basicAuthenticatorHeader("Authorization", USERNAME, PASSWORD) }

    fun loadSampleList(page: Int) = CSPingRequest(this) {
        client.get(this, "sampleList", CSListServerData("list", ServerListItem::class)
            , mapOf("pageNumber" to "$page"))
    }

    fun addSampleListItem(item: ServerListItem) = CSPingRequest(this) {
        client.post("sampleList/add", item, CSValueServerData(ServerListItem::class))
    }

    fun deleteSampleListItems(items: List<ServerListItem>) = CSPingConcurrentRequest(this) {
        items.forEach { item -> it.add(client.post("sampleList/delete", mapOf("id" to item.id))) }
    }

    override fun ping() = client.get("ping")
}

class ServerListItem : CSServerData() {
    val id get() = getString("id")!!
    val image get() = getString("image")!!
    val name = CSJsonString(this, "name")
    val description = CSJsonString(this, "description")
}