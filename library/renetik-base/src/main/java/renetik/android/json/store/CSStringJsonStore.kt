package renetik.android.json.store

class CSStringJsonStore(var jsonString: String) : CSJsonStore() {
    override fun loadJsonString() = jsonString
    override fun saveJsonString(json: String) {
        jsonString = json
    }
}