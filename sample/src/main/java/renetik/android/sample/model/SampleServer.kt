package renetik.android.sample.model

import renetik.android.client.okhttp3.CSOkHttpClient
import renetik.android.client.okhttp3.get
import renetik.android.client.okhttp3.post
import renetik.android.client.request.CSConcurrentResponse
import renetik.android.client.request.CSPingResponse
import renetik.android.client.request.CSRequest
import renetik.android.client.request.CSServerWithPing
import renetik.android.java.collections.list
import renetik.android.java.collections.map
import renetik.android.json.data.CSJsonData
import renetik.android.json.data.properties.CSJsonStringProperty

const val SERVER_URL = "https://renetik-library-server.herokuapp.com/api"
const val SERVER_DEV_URL = "http://localhost:8080/api"
const val USERNAME = "username"
const val PASSWORD = "password"

class SampleServer : CSServerWithPing {

    private val client = CSOkHttpClient(SERVER_URL).apply {
        basicAuthenticatorHeader("Authorization", USERNAME, PASSWORD)
    }

    fun loadSampleList(pageNumber: Int, searchText: String) = CSRequest {
        CSPingResponse(this) {
            client.get("sampleList", ListServerData("list", SampleListItem::class)
                    , map("pageNumber", "$pageNumber", "searchText", searchText))
        }
    }

    fun addSampleListItem(item: SampleListItem) = CSRequest {
        CSPingResponse(this) { client.post("sampleList/add", item) }
    }

    fun deleteSampleListItems(items: List<SampleListItem>) = CSRequest {
        CSPingResponse(this) {
            CSConcurrentResponse(list()).apply {
                items.forEach { item -> add(client.post("sampleList/delete", map("item", "${item.id}"))) }
            }
        }
    }

    override fun ping() = client.get("ping")
}

class SampleListItem : CSJsonData() {
    val id get() = getLong("id")!!
    val image get() = getString("image")!!
    private val nameProperty = CSJsonStringProperty(this, "name")
    var name
        get() = nameProperty.value
        set(value) {
            nameProperty.string = value
        }
    private val descriptionProperty = CSJsonStringProperty(this, "description")
    var description
        get() = descriptionProperty.value
        set(value) {
            descriptionProperty.string = value
        }
}