package renetik.android.ui.extensions.widget

import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.PixelFormat.TRANSLUCENT
import android.graphics.PorterDuff.Mode.SRC_IN
import android.graphics.PorterDuffColorFilter
import android.graphics.Shader.TileMode
import android.graphics.Shader.TileMode.REPEAT
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import renetik.android.core.extensions.content.drawable
import renetik.android.core.kotlin.primitives.alphaInt

class CSTileDrawable(
    src: Drawable, tileMode: TileMode = REPEAT
) : Drawable() {

    companion object {
        fun ImageView.imagePattern(
            @DrawableRes drawableRes: Int, alpha: Float = 1f,
            @ColorInt tint: Int? = null, mode: TileMode = REPEAT
        ) = setImageDrawable(CSTileDrawable(context.drawable(drawableRes), mode)
            .apply { setAlpha(alpha.alphaInt); tint?.let(::setTint) })
    }

    private val bitmap: Bitmap = src.mutate().toBitmap(
        src.intrinsicWidth, src.intrinsicHeight, ARGB_8888)

    private val paint = Paint(ANTI_ALIAS_FLAG).apply {
        shader = BitmapShader(bitmap, tileMode, tileMode)
    }

    override fun draw(canvas: Canvas) = canvas.drawPaint(paint)

    override fun setAlpha(a: Int) {
        paint.alpha = a
        invalidateSelf()
    }

    override fun setTint(@ColorInt tintColor: Int) {
        paint.colorFilter = PorterDuffColorFilter(tintColor, SRC_IN)
        invalidateSelf()
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
        invalidateSelf()
    }

    @Deprecated("Deprecated in Java",
        ReplaceWith("PixelFormat.TRANSLUCENT", "android.graphics.PixelFormat"))
    override fun getOpacity(): Int = TRANSLUCENT
}