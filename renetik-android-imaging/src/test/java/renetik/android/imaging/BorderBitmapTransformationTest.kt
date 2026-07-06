package renetik.android.imaging

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.createBitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPoolAdapter
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import renetik.android.testing.CSAssert.assert
import renetik.android.testing.TestApplication
import java.security.MessageDigest

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class BorderBitmapTransformationTest {

    private fun transform(
        transformation: BorderBitmapTransformation, bitmap: Bitmap
    ): Bitmap {
        val method = BorderBitmapTransformation::class.java.getDeclaredMethod(
            "transform", BitmapPool::class.java, Bitmap::class.java,
            Int::class.java, Int::class.java)
        method.isAccessible = true
        return method.invoke(
            transformation, BitmapPoolAdapter(), bitmap, bitmap.width, bitmap.height
        ) as Bitmap
    }

    @Test
    fun outputKeepsInputDimensions() {
        val input = createBitmap(32, 32).apply { eraseColor(Color.WHITE) }
        val output = transform(BorderBitmapTransformation(color = Color.BLACK), input)
        assertNotNull(output)
        assert(expected = 32, actual = output.width)
        assert(expected = 32, actual = output.height)
    }

    @Test
    fun transformReturnsNewUsableBitmap() {
        val input = createBitmap(32, 32).apply { eraseColor(Color.WHITE) }
        val output = transform(BorderBitmapTransformation(borderWidth = 4f), input)
        assertNotEquals(input, output)
        assertFalse(output.isRecycled)
    }

    @Test
    fun diskCacheKeyIsStable() {
        val digest1 = MessageDigest.getInstance("SHA-256")
        val digest2 = MessageDigest.getInstance("SHA-256")
        BorderBitmapTransformation().updateDiskCacheKey(digest1)
        BorderBitmapTransformation().updateDiskCacheKey(digest2)
        assert(expected = digest1.digest().toList(), actual = digest2.digest().toList())
    }
}
