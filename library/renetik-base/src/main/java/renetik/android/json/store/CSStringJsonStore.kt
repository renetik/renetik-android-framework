package renetik.android.json.store

import renetik.android.java.extensions.collections.reload

class CSStringJsonStore(jsonString: String) : CSJsonStore() {

    var jsonString: String = jsonString
        set(value) {
            field = value
            data.reload(load())
        }

    override fun loadJsonString() = jsonString
    override fun saveJsonString(json: String) {
        jsonString = json
    }
}