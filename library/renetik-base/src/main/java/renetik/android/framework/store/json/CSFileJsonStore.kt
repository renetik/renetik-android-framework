package renetik.android.framework.store.json

import android.content.Context
import renetik.android.core.CSApplication.Companion.app
import renetik.android.core.java.io.readString
import renetik.android.core.java.io.write
import renetik.android.framework.store.json.CSJsonStore
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

