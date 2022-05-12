package renetik.android.framework.json.store

import android.content.Context
import renetik.android.framework.CSApplication.Companion.app
import renetik.java.io.readString
import renetik.java.io.write
import java.io.File

class CSFileJsonStore(context: Context, id: String, directory: String = "",
                      isJsonPretty: Boolean = false)
    : CSJsonStore(isJsonPretty) {

    constructor(id: String, directory: String = "", isJsonPretty: Boolean = false)
            : this(app, id, directory, isJsonPretty)

    private val file = File(File(context.filesDir, directory), "$id.json")
    override fun loadJsonString() = file.readString()
    override fun saveJsonString(json: String) {
        file.write(json)
    }
}

