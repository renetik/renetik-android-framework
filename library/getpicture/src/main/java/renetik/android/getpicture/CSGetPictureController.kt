package renetik.android.getpicture

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.Intent.createChooser
import android.graphics.Bitmap
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.provider.MediaStore.EXTRA_OUTPUT
import android.view.View
import androidx.core.content.FileProvider.getUriForFile
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions.overrideOf
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import renetik.android.base.CSView
import renetik.android.base.application
import renetik.android.controller.base.CSViewController
import renetik.android.controller.base.startActivityForResult
import renetik.android.controller.extension.requestPermissions
import renetik.android.dialog.extensions.dialog
import renetik.android.extensions.snackBarWarn
import renetik.android.logging.CSLog.logInfo
import renetik.java.collections.list
import renetik.java.extensions.createDatedFile
import renetik.java.lang.tryAndError
import java.io.File

class CSGetPictureController<T : View>(val parent: CSViewController<T>, val title: String, private val folder: File,
                                       private val onImageReady: (File) -> Unit) : CSViewController<T>(parent) {

    constructor(parent: CSViewController<T>, title: String, imagesDirName: String, onImageReady: (File) -> Unit) :
            this(parent, title, File(File(application.externalFilesDir, "Pictures"), imagesDirName), onImageReady)

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

    private fun onImageSelected(input: Any) = tryAndError {
        doAsync {
            tryAndError {
                val outputImage = folder.createDatedFile("jpg")
                outputImage.outputStream().use { stream ->
                    Glide.with(view).asBitmap().load(input).apply(
                            overrideOf(1024, 768).centerInside()
                                    .diskCacheStrategy(DiskCacheStrategy.NONE))
                            .submit(0, 0).get().compress(Bitmap.CompressFormat.JPEG, 80, stream)
                }
                uiThread { onImageReady(outputImage) }
            }
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
        intent.putExtra(EXTRA_OUTPUT, getUriForFile(this, "renetik.android.getpicture.fileprovider",
                application.getExternalFilesDir(DIRECTORY_PICTURES)!!.createDatedFile("jpg")))
        startActivityForResult(intent) {
            logInfo(it)
            onImageSelected(intent.getParcelableExtra(EXTRA_OUTPUT))
        }
    }
}