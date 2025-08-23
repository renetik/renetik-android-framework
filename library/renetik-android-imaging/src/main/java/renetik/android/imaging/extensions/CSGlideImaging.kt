package renetik.android.imaging.extensions

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.view.doOnLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy.AUTOMATIC
import com.bumptech.glide.request.RequestOptions
import renetik.android.ui.extensions.view.hasSize
import renetik.android.ui.extensions.widget.CSImaging
import java.io.File

class CSGlideImaging : CSImaging {
    companion object {
        fun initialize() = CSGlideImaging().also { CSImaging.instance = it }
    }

    private fun ImageView.load(data: Any?) {
        val glide = Glide.with(this)
        if (data == null) {
            setImageDrawable(null)
            glide.clear(this)
            return
        }
        var request = glide.load(data).diskCacheStrategy(AUTOMATIC).dontAnimate()
        if (hasSize) {
            request.apply(RequestOptions().override(width, height)).into(this)
        } else {
            setImageDrawable(null)
            glide.clear(this)
            doOnLayout {
                if (hasSize)
                    request = request.apply(RequestOptions().override(width, height))
                request.into(this)
            }
        }
    }

    override fun load(view: ImageView, @DrawableRes resourceId: Int?) = view.load(resourceId)
    override fun load(view: ImageView, file: File) = view.load(file)
    override fun load(view: ImageView, bitmap: Bitmap?) = view.load(bitmap)
    override fun load(view: ImageView, drawable: Drawable?) = view.load(drawable)
    override fun load(view: ImageView, uri: Uri?) = view.load(uri)
}