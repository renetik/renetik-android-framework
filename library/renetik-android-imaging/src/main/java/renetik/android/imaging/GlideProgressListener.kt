package renetik.android.imaging.extensions

import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import renetik.android.core.lang.Fun

class CSGlideProgressListener(
    private val progress: View,
    private val onFailed: Fun? = null
) : RequestListener<Drawable> {
    init {
        progress.visibility = View.VISIBLE
    }

    override fun onLoadFailed(
        e: GlideException?, model: Any?, target: Target<Drawable>,
        isFirstResource: Boolean
    ): Boolean {
        progress.visibility = View.GONE
        onFailed?.invoke()
        return false
    }

    override fun onResourceReady(
        resource: Drawable, model: Any, target: Target<Drawable>?,
        dataSource: DataSource, isFirstResource: Boolean
    ): Boolean {
        progress.visibility = View.GONE
        return false
    }
}