package renetik.android.imaging

import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.core.graphics.createBitmap
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment.getApplication
import org.robolectric.annotation.Config
import renetik.android.core.lang.CSEnvironment
import renetik.android.testing.TestApplication
import renetik.android.testing.context
import java.io.ByteArrayOutputStream
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class UriImagingTest {

    @Before
    fun prepare() {
        CSEnvironment.app = getApplication()
    }

    private fun imageFile(width: Int, height: Int): File {
        val file = File(context.cacheDir, "uri-imaging-${System.nanoTime()}.jpg")
        file.write(createBitmap(width, height).apply { eraseColor(Color.GREEN) })
        return file
    }

    @Test
    fun resizeImageWritesScaledJpeg() {
        val uri = Uri.fromFile(imageFile(width = 64, height = 32))
        val output = ByteArrayOutputStream()
        uri.resizeImage(maxTargetWidth = 16, maxTargetHeight = 16, output)
        val bytes = output.toByteArray()
        assertTrue(bytes.isNotEmpty())
        val resized = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        assertNotNull(resized)
        assertTrue(resized!!.width <= 16)
    }

    @Test
    fun createFixOrientationMatrixIsIdentityForPlainJpeg() {
        val uri = Uri.fromFile(imageFile(width = 8, height = 8))
        assertTrue(uri.createFixOrientationMatrix().isIdentity)
    }
}
