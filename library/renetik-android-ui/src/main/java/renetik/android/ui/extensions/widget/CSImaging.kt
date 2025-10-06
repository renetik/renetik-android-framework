package renetik.android.ui.extensions.widget

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import java.io.File

interface CSImaging {
    companion object {
         var instance: CSImaging = CSAndroidImaging()
    }

    fun load(view: ImageView, uri: Uri?)
    fun load(view: ImageView, file: File)
    fun load(view: ImageView, bitmap: Bitmap?)
    fun load(view: ImageView, drawable: Drawable?)
    fun load(view: ImageView, @DrawableRes resourceId: Int?)
}