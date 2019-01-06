package renetik.android.imaging.extensions

import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.net.Uri
import android.widget.ImageView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority.MEDIUM
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.BitmapRequestListener
import renetik.android.base.application
import renetik.android.logging.CSLog
import renetik.android.view.extensions.hasSize
import java.io.File

fun <T : ImageView> T.image(file: File) = apply {
    //    Glide.with(this).load(file).into(this)
    setImageURI(Uri.fromFile(file))
}

fun <T : ImageView> T.image(url: String) = apply {
    //    Glide.with(this).load(url).into(this)
    hasSize {
        AndroidNetworking.get(url)
                .setTag(application.name).setPriority(MEDIUM).setBitmapConfig(ARGB_8888)
                .setBitmapMaxWidth(width).setBitmapMaxHeight(height).build()
                .getAsBitmap(object : BitmapRequestListener {
                    override fun onResponse(bitmap: Bitmap) {
                        setImageBitmap(bitmap)
                    }

                    override fun onError(error: ANError) {
                        CSLog.logError(error)
                    }
                })
    }
}

fun <T : ImageView> T.image(resourceId: Int) = apply {
    setImageResource(resourceId)
//    Glide.with(this).load(resourceId).into(this)
}