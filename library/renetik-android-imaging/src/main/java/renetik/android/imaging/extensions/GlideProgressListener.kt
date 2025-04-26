package renetik.android.imaging.extensions

import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import renetik.android.core.lang.Func
import renetik.android.ui.extensions.view.gone
import renetik.android.ui.extensions.view.show

class CSGlideProgressListener(
    private val progress: View,
    private val onFailed: Func? = null
) : RequestListener<Drawable> {
    init {
        progress.show()
    }

    override fun onLoadFailed(
        e: GlideException?, model: Any?, target: Target<Drawable>,
        isFirstResource: Boolean
    ): Boolean {
        progress.gone()
        onFailed?.invoke()
        return false
    }

    override fun onResourceReady(
        resource: Drawable, model: Any, target: Target<Drawable>?,
        dataSource: DataSource, isFirstResource: Boolean
    ): Boolean {
        progress.gone()
        return false
    }
}