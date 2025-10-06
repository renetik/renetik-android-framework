package renetik.android.ui.extensions.widget

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import renetik.android.core.kotlin.unexpected
import java.io.File

class CSAndroidImaging : CSImaging {
    companion object {
        fun initialize() = CSAndroidImaging().also { CSImaging.instance = it }
    }

    override fun load(view: ImageView, uri: Uri?): Unit = unexpected()
    override fun load(view: ImageView, file: File) = view.setImageURI(Uri.fromFile(file))
    override fun load(view: ImageView, bitmap: Bitmap?) = view.setImageBitmap(bitmap)
    override fun load(view: ImageView, drawable: Drawable?) = view.setImageDrawable(drawable)
    override fun load(view: ImageView, resourceId: Int?) =
        resourceId?.let(view::setImageResource) ?: view.setImageDrawable(null)
}