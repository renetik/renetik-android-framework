package renetik.android.framework.json.data.properties

import renetik.android.framework.json.data.CSJsonMapStore
import java.io.File

class CSJsonFile(val data: CSJsonMapStore, private val key: String) {
    var file: File?
        get() = data.getString(key)?.let { File(it) }
        set(file) {
            data.save(key, file?.toString())
        }

    val value: File get() = file!!

    override fun toString() = "$file"
}