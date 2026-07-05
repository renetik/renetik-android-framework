package renetik.android.imaging.extensions

import android.graphics.drawable.Drawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import renetik.android.core.lang.Fun

open class DrawableRequestAdapter(
    val onFailed: Fun? = null,
    val onReady: Fun? = null
) : RequestListener<Drawable> {
    override fun onLoadFailed(
        e: GlideException?, model: Any?,
        target: Target<Drawable?>, isFirstResource: Boolean
    ): Boolean {
        onLoadFailed()
        return false
    }

    open fun onLoadFailed() = onFailed?.invoke()

    override fun onResourceReady(
        resource: Drawable, model: Any,
        target: Target<Drawable?>?, dataSource: DataSource,
        isFirstResource: Boolean
    ): Boolean {
        onResourceReady()
        return false
    }

    open fun onResourceReady() = onReady?.invoke()
}