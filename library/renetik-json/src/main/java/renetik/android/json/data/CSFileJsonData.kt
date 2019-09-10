package renetik.android.json.data

import renetik.android.json.data.properties.CSJsonFile
import java.io.File

class CSFileJsonData() : CSJsonData() {

    val file get() = fileProperty.file

    constructor(file: File) : this() {
        fileProperty.file = file
    }

    private val fileProperty = CSJsonFile(this, "file")
}