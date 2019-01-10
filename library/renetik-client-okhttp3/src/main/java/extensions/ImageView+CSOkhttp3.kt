package renetik.android.imaging.extensions

import android.widget.ImageView
import com.androidnetworking.error.ANError
import com.androidnetworking.internal.ANImageLoader
import renetik.android.view.extensions.hasSize

fun <T : ImageView> T.image(url: String, defaultImageId: Int? = null) = apply {
    hasSize {
        ANImageLoader.getInstance().get(url,
                object : ANImageLoader.ImageListener {
                    override fun onResponse(response: ANImageLoader.ImageContainer, isImmediate: Boolean) {
                        response.bitmap?.let { setImageBitmap(response.bitmap) }
                                ?: defaultImageId?.apply { setImageResource(this) }
                    }

                    override fun onError(error: ANError) {
                        defaultImageId?.apply { setImageResource(this) }
                    }
                }, width, height, scaleType)
    }
}