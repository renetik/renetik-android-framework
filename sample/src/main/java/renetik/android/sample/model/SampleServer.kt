package renetik.android.sample.model

import renetik.android.client.okhttp3.CSOkHttpClient
import renetik.android.client.okhttp3.get
import renetik.android.client.okhttp3.post
import renetik.android.client.request.CSConcurrentResponse
import renetik.android.client.request.CSRequest
import renetik.android.client.request.CSServerData
import renetik.android.java.collections.map
import renetik.android.json.data.CSJsonData

const val SERVER_URL = "https://renetik-library-server.herokuapp.com/api"
const val SERVER_DEV_URL = "http://localhost:8080/api"
const val USERNAME = "username"
const val PASSWORD = "password"

class SampleServer {

    private val client = CSOkHttpClient(SERVER_URL).apply {
        basicAuthenticatorHeader("Authorization", USERNAME, PASSWORD)
    }

    fun loadSampleList(pageNumber: Int, searchText: String) = CSRequest {
        client.get("sampleList", ListServerData("list", ListItemData::class)
                , map("pageNumber", "$pageNumber", "searchText", searchText))
    }

    fun addSampleListItem(item: ListItemData) = CSRequest { client.post("sampleList/add", item) }

    fun deleteSampleListItems(items: List<ListItemData>) = CSRequest {
        CSConcurrentResponse().apply {
            items.forEach { item -> add(client.post("sampleList/delete", map("item", "${item.id}"))) }
        }
    }

    private fun ping() = CSRequest { client.get("${SERVER_URL}greeting", CSServerData()) }
}

class ListItemData : CSJsonData() {
    val id get() = getLong("id")!!
    val image get() = getString("image")!!
    val name get() = getString("name")!!
    val description get() = getString("description")!!
}