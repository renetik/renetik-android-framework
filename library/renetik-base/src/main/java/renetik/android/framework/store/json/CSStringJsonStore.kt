package renetik.android.framework.store.json

import renetik.android.core.kotlin.collections.reload
import renetik.android.framework.store.json.CSJsonStore

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