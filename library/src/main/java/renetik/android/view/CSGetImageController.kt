package renetik.android.view

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.content.Intent.createChooser
import android.net.Uri
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.provider.MediaStore.EXTRA_OUTPUT
import android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
import android.view.View
import renetik.android.application
import renetik.android.extensions.dialog
import renetik.android.extensions.requestPermissions
import renetik.android.extensions.snackBarWarn
import renetik.android.image.CSBitmap.resizeImage
import renetik.android.java.collections.list
import renetik.android.lang.CSLang.copy
import renetik.android.lang.CSLang.generateRandomStringOfLength
import renetik.android.lang.tryAndError
import renetik.android.lang.tryAndWarn
import renetik.android.viewbase.CSView
import renetik.android.viewbase.CSViewController
import renetik.android.viewbase.startActivityForResult
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class CSGetImageController<T : View>(parent: CSViewController<T>, val title: String, private val folder: File,
                                     private val onImageReady: (File) -> Unit) : CSViewController<T>(parent) {

    constructor(parent: CSViewController<T>, title: String, imagesDirName: String, onImageReady: (File) -> Unit) :
            this(parent, title, File(File(application.externalFilesDir, "Pictures"), imagesDirName), onImageReady)

    var selectPhoto = true
    var takePhoto = true
    private val REQUEST_CODE = 386
    private val photoURI: Uri by lazy { contentResolver.insert(EXTERNAL_CONTENT_URI, ContentValues()) }

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

    private fun onImageSelected(input: InputStream) = tryAndError {
        val image = File(folder, generateRandomStringOfLength(4) + ".jpg")
        image.createNewFile()
        copy(input, FileOutputStream(image))
        resizeImage(image.absolutePath, 1024, 768)
        onImageReady(image)
    }

    private fun onSelectPhoto() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        onActivityResult.run { registration, result ->
            if (result.requestCode == REQUEST_CODE) {
                if (result.resultCode == RESULT_OK)
                    tryAndWarn { onImageSelected(openInputStream(result.data!!.data!!)!!) }
                registration.cancel()
            }
        }
        startActivityForResult(createChooser(intent, "Select Picture"), REQUEST_CODE)
    }

    private fun onTakePhoto() {
        val intent = Intent(ACTION_IMAGE_CAPTURE)
        intent.putExtra(EXTRA_OUTPUT, photoURI)
        onActivityResult.run { registration, result ->
            if (result.isOK(REQUEST_CODE))
                tryAndWarn { onImageSelected(openInputStream(photoURI)!!) }
            registration.cancel()
        }
        startActivityForResult(intent, REQUEST_CODE)
    }

}