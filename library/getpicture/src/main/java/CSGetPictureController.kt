package renetik.android.getpicture

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.Intent.createChooser
import android.net.Uri
import android.os.Environment.getExternalStorageDirectory
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.provider.MediaStore.EXTRA_OUTPUT
import android.view.View
import androidx.core.content.FileProvider.getUriForFile
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import renetik.android.base.CSView
import renetik.android.base.application
import renetik.android.controller.base.CSViewController
import renetik.android.controller.base.startActivityForResult
import renetik.android.controller.extensions.requestPermissions
import renetik.android.dialog.extensions.dialog
import renetik.android.imaging.extensions.resizeImage
import renetik.android.java.collections.list
import renetik.android.java.common.tryAndError
import renetik.android.java.extensions.createDatedFile
import renetik.android.material.extensions.snackBarWarn
import java.io.File

class CSGetPictureController<T : View>(val parent: CSViewController<T>, val title: String, private val folder: File,
                                       private val onImageReady: (File) -> Unit) : CSViewController<T>(parent) {

    constructor(parent: CSViewController<T>, title: String, imagesDirName: String, onImageReady: (File) -> Unit) :
            this(parent, title, File(File(application.externalFilesDir, "Pictures"), imagesDirName), onImageReady)

    private val cacheImagesDir = File(getExternalStorageDirectory(),
            "Android/data/renetik.android.getpicture/files/ImageCache")
    var selectPhoto = true
    var takePhoto = true

    init {
        folder.mkdirs()
    }

    override fun show(): CSView<T> {
        requestPermissions(list(CAMERA, WRITE_EXTERNAL_STORAGE), { showAfterPermissionsGranted() },
                onNotGranted = { snackBarWarn("Some permissions not granted for taking photos") })
        return this
    }

    private fun showAfterPermissionsGranted() {
        if (!selectPhoto) onTakePhoto()
        else if (!takePhoto) onSelectPhoto()
        else dialog(title).showChoice("Album", { onSelectPhoto() }, "Camera", { onTakePhoto() })
    }

    private fun onImageSelected(input: Uri, onDone: (() -> Unit)? = null) = tryAndError {
        doAsync {
            tryAndError {
                val outputImage = folder.createDatedFile("jpg")
                outputImage.outputStream().use { output -> input.resizeImage(1024, 768, output) }
                uiThread { onImageReady(outputImage) }
            }
            uiThread { onDone?.invoke() }
        }
    }

    private fun onSelectPhoto() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(createChooser(intent, "Select Picture")) { result ->
            tryAndError { onImageSelected(result!!.data!!) }
        }
    }

    private fun onTakePhoto() {
        val intent = Intent(ACTION_IMAGE_CAPTURE)
        val cacheImage = cacheImagesDir.createDatedFile("jpg")
        val authority = "${applicationContext.packageName}.renetik.android.getpicture.fileprovider"
        val uri = getUriForFile(this, authority, cacheImage)
        startActivityForResult(intent.putExtra(EXTRA_OUTPUT, uri)) {
            onImageSelected(uri) { cacheImage.delete() }
        }
    }
}