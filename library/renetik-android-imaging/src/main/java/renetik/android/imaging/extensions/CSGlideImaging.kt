package renetik.android.imaging.extensions

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import renetik.android.ui.extensions.view.hasSize
import renetik.android.ui.extensions.widget.CSImaging
import java.io.File

class CSGlideImaging : CSImaging {
    companion object {
        fun initialize() = CSGlideImaging().also { CSImaging.instance = it }
    }

    private fun ImageView.load(data: Any?) {
        setImageDrawable(null)
        val glide = Glide.with(this)
        glide.clear(this)
        if (data == null) return
        val request = glide.load(data)
        if (hasSize) request.apply(RequestOptions().override(width, height)).into(this)
        else request.into(this)
    }

    override fun load(view: ImageView, @DrawableRes resourceId: Int?) = view.load(resourceId)
    override fun load(view: ImageView, file: File) = view.load(file)
    override fun load(view: ImageView, bitmap: Bitmap?) = view.load(bitmap)
    override fun load(view: ImageView, drawable: Drawable?) = view.load(drawable)
    override fun load(view: ImageView, uri: Uri?) = view.load(uri)
}