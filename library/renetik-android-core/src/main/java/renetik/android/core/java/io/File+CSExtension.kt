package renetik.android.core.java.io

import renetik.android.core.java.util.format
import java.io.File
import java.io.File.createTempFile
import java.util.*


fun File.createFileAndDirs() = apply {
    parentFile?.mkdirs()
    createNewFile()
}

fun File.createFileDirs() = apply {
    parentFile?.mkdirs()
}

fun File.write(text: String) = apply {
    createFileAndDirs()
    writeText(text)
}

fun File.recreateDirs() = apply {
    deleteRecursively()
    mkdirs()
}

fun File.createDirs() = apply {
    mkdirs()
}

fun File.createDatedFile(extension: String): File {
    mkdirs()
    return createTempFile(Date().format("yyyy-MM-dd_HH-mm-ss"), ".$extension", this)
}

fun File.readString() = if (exists()) readText() else null

val File.isDirEmpty get() = list()?.isEmpty() ?: !exists()

fun File.moveTo(file: File, overwrite: Boolean = true) {
    copyTo(file, overwrite)
    delete()
}