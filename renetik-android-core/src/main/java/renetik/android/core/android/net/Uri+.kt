package renetik.android.core.android.net

import android.net.Uri
import android.util.Base64
import androidx.core.net.toUri
import renetik.android.core.base.CSApplication.Companion.app
import renetik.android.core.kotlin.unexpected
import java.io.File
import java.io.File.createTempFile
import java.io.FileOutputStream

fun Uri.toCacheUploadFile(): File {
    val inputStream = app.contentResolver.openInputStream(this) ?: unexpected()
    val tempFile = createTempFile("upload_", ".jpg", app.cacheDir)
    FileOutputStream(tempFile).use { inputStream.copyTo(it) }
    return tempFile
}

fun Uri.toUploadImageFileUri(): Uri = when (scheme) {
    "file" -> this
    "content" -> createTempUploadImageFile().also { file ->
        app.contentResolver.openInputStream(this)?.use { input ->
            file.outputStream().use(input::copyTo)
        }
    }.toUri()
    else -> unexpected()
}

fun createTempUploadImageFile() = createTempFile("upload_image_", ".jpg", app.cacheDir)

fun Uri.encodeToBase64(): String {
    val bytes = app.activity!!.contentResolver
        .openInputStream(this)!!.use { it.readBytes() }
    return Base64.encodeToString(bytes, Base64.NO_WRAP)
}

