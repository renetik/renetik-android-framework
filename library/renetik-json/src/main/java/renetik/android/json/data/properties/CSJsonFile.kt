package renetik.android.json.data.properties

import renetik.android.json.data.CSJsonMap
import renetik.android.json.data.extensions.getString
import renetik.android.json.data.extensions.put
import java.io.File

class CSJsonFile(val data: CSJsonMap, private val key: String) {
    var file: File?
        get() = data.getString(key)?.let { File(it) }
        set(file) {
            data.put(key, file?.toString())
        }

    val value: File get() = file!!

    override fun toString() = "$file"
}