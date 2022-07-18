package renetik.android.framework.controller.getpicture

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.Intent.createChooser
import android.net.Uri
import android.os.Environment.getExternalStorageDirectory
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.provider.MediaStore.EXTRA_OUTPUT
import android.view.View
import androidx.core.content.FileProvider.getUriForFile
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.extensions.requestPermissions
import renetik.android.controller.extensions.startActivityForResult
import renetik.android.core.extensions.content.Intent
import renetik.android.core.extensions.content.externalFilesDir
import renetik.android.core.java.io.createDatedFile
import renetik.android.core.kotlin.collections.list
import renetik.android.core.kotlin.unfinished
import renetik.android.core.lang.CSBackground.background
import renetik.android.core.lang.CSEnvironment.app
import renetik.android.core.lang.Func
import renetik.android.core.lang.catchAllError
import renetik.android.core.logging.CSLog.logWarn
import renetik.android.core.logging.CSLogMessage.Companion.message
import renetik.android.imaging.extensions.resizeImage
import java.io.File

// TODO: Not working right now...
class CSGetPictureView<T : View>(
    parent: CSActivityView<T>, val title: String, private val folder: File,
    private val onImageReady: (File) -> Unit) : CSActivityView<T>(parent) {

    constructor(parent: CSActivityView<T>, title: String,
                imagesDirName: String, onImageReady: (File) -> Unit) :
            this(parent, title, File(File(app.externalFilesDir, "Pictures"),
                imagesDirName), onImageReady)

    //TODO: Migrate to Context#getExternalFilesDir(String)
    private val cacheImagesDir = File(getExternalStorageDirectory(),
        "Android/data/renetik.android.getpicture/files/ImageCache")
    var selectPhoto = true
    var takePhoto = true

    init {
        folder.mkdirs()
    }

    fun show(onPermissionsNotGranted: Func? = null) {
        requestPermissions(list(CAMERA, WRITE_EXTERNAL_STORAGE), { showAfterPermissionsGranted() },
            notGranted = {
                logWarn { message("Some permissions not granted for taking photos") }
                onPermissionsNotGranted?.invoke()
            })
    }

    private fun showAfterPermissionsGranted() {
        if (!selectPhoto) onTakePhoto()
        else if (!takePhoto) onSelectPhoto()
        unfinished()
//    TODO!!!  else dialog(title).showChoice("Album", { onSelectPhoto() }, "Camera", { onTakePhoto() })
    }

    private fun onImageSelected(input: Uri, onDone: (() -> Unit)? = null) = catchAllError {
        background {
            catchAllError {
                val outputImage = folder.createDatedFile("jpg")
                outputImage.outputStream().use { output -> input.resizeImage(1024, 768, output) }
                onMain { onImageReady(outputImage) }
            }
            onMain { onDone?.invoke() }
        }
    }

    private fun onSelectPhoto() {
        startActivityForResult(createChooser(Intent(ACTION_GET_CONTENT, "image/*"),
            "Select Picture"), { catchAllError { onImageSelected(it!!.data!!) } })
    }

    private fun onTakePhoto() {
        val intent = Intent(ACTION_IMAGE_CAPTURE)
        val cacheImage = cacheImagesDir.createDatedFile("jpg")
        val authority = "${applicationContext.packageName}.renetik.android.getpicture.fileprovider"
        val uri = getUriForFile(this, authority, cacheImage)
        startActivityForResult(intent.putExtra(EXTRA_OUTPUT, uri), {
            onImageSelected(uri) { cacheImage.delete() }
        })
    }
}