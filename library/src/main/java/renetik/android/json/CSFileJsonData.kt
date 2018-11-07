package renetik.android.json

import java.io.File

class CSFileJsonData() : CSJsonData() {

    val file get() = fileProperty.value

    constructor(file: File) : this() {
        fileProperty.value = file
    }

    private val fileProperty = CSJsonFileProperty(this, "file")
}