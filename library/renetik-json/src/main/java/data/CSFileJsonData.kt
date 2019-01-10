package renetik.android.json.data

import renetik.android.json.data.properties.CSJsonFileProperty
import java.io.File

class CSFileJsonData() : CSJsonData() {

    val file get() = fileProperty.file

    constructor(file: File) : this() {
        fileProperty.file = file
    }

    private val fileProperty = CSJsonFileProperty(this, "file")
}