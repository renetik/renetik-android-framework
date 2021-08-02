package renetik.android.json.store

import renetik.android.java.extensions.readString
import renetik.android.java.extensions.write
import java.io.File

class CSFileJsonStore(id: String, directory: String = CSFileJsonStore::class.simpleName!!)
    : CSJsonStore() {
    private val file = File(File(filesDir, directory), "$id.json")
    override fun loadJsonString() = file.readString()
    override fun saveJsonString(json: String) {
        file.write(json)
    }
}
