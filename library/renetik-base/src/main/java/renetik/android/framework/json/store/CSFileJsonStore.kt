package renetik.android.framework.json.store

import renetik.android.framework.CSApplication.Companion.application
import renetik.java.io.readString
import renetik.java.io.write
import java.io.File

class CSFileJsonStore(id: String, directory: String = "", isJsonPretty: Boolean = false)
    : CSJsonStore(isJsonPretty) {
    private val file = File(File(application.filesDir, directory), "$id.json")
    override fun loadJsonString() = file.readString()
    override fun saveJsonString(json: String) {
        file.write(json)
    }
}

