package renetik.android.ui.extensions.widget

import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat.getDrawable

class CSTileDrawable(drawable: Drawable, tileMode: Shader.TileMode) : Drawable() {

    companion object {
        fun ImageView.setImagePattern(
            @DrawableRes drawable: Int, alpha: Float,
            @ColorInt tint: Int, mode: Shader.TileMode = Shader.TileMode.REPEAT
        ) {
            val tileDrawable = CSTileDrawable(getDrawable(context, drawable)!!, mode)
            tileDrawable.alpha = (255 * alpha).toInt()
            tileDrawable.setTint(tint)
            setImageDrawable(tileDrawable)
        }
    }

    private var alpha: Int? = null

    override fun setAlpha(alpha: Int) {
        this.alpha = alpha
    }

    private var tintColor: Int? = null

    override fun setTint(tintColor: Int) {
        this.tintColor = tintColor
    }

    private val paint by lazy {
        Paint().also { paint ->
            paint.shader = BitmapShader(getBitmap(drawable), tileMode, tileMode)
            alpha?.let { paint.alpha = it }
        }
    }

    override fun draw(canvas: Canvas) = canvas.drawPaint(paint)

    @Deprecated("Needed to override and mark as detracted because of warning")
    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) = Unit

    private fun getBitmap(drawable: Drawable): Bitmap {
        val constantState = drawable.constantState ?: drawable.mutate().constantState
        val stateDrawable = constantState?.newDrawable() ?: drawable
        tintColor?.let(stateDrawable::setTint)
        if (stateDrawable is BitmapDrawable) return stateDrawable.bitmap
        val bitmap = Bitmap.createBitmap(
            stateDrawable.intrinsicWidth, stateDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        stateDrawable.setBounds(0, 0, stateDrawable.intrinsicWidth, stateDrawable.intrinsicHeight)
        stateDrawable.draw(canvas)
        return bitmap
    }
}