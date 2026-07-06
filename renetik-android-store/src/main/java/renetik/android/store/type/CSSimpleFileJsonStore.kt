package renetik.android.store.type

import renetik.android.core.base.CSApplication.Companion.app
import renetik.android.core.java.io.readString
import renetik.android.core.java.io.write
import renetik.android.core.kotlin.changeIf
import renetik.android.core.lang.CSEnvironment
import renetik.android.json.CSJson
import renetik.android.json.parseJsonMap
import renetik.android.json.toJson
import java.io.File

class CSSimpleFileJsonStore(
    val file: File,
    private val isJsonPretty: Boolean = CSEnvironment.isDebug
) : CSJsonObjectStore() {

    companion object {
        fun CSSimpleFileJsonStore(
            fileName: String,
            isJsonPretty: Boolean = CSJson.isJsonPretty,
        ) = CSSimpleFileJsonStore(
            File(app.filesDir, "$fileName.json"),
            isJsonPretty
        )
    }

    override val data: MutableMap<String, Any?> =
        file.readString()?.parseJsonMap() ?: mutableMapOf()

    fun save() = file.write(data.changeIf(isJsonPretty) { toSortedMap() }
        .toJson(formatted = isJsonPretty))
}