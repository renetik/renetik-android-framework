package renetik.android.imaging

import android.graphics.Bitmap
import android.graphics.Color
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
import renetik.android.testing.CSAssert.assert
import renetik.android.testing.TestApplication
import renetik.android.testing.context
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class FileImagingTest {

    @Before
    fun prepare() {
        CSEnvironment.app = getApplication()
    }

    private fun newBitmap(width: Int = 32, height: Int = 16): Bitmap =
        createBitmap(width, height).apply { eraseColor(Color.RED) }

    private fun tempImageFile(): File =
        File(context.cacheDir, "imaging/test-${System.nanoTime()}.jpg")

    @Test
    fun writeCreatesFileAndDirs() {
        val file = tempImageFile()
        file.write(newBitmap())
        assertTrue(file.exists())
        assertTrue(file.length() > 0)
    }

    @Test
    fun writeThenLoadBitmapRoundTrip() {
        val file = tempImageFile()
        file.write(newBitmap(width = 32, height = 16))
        val loaded = file.loadBitmap()
        assertNotNull(loaded)
        assert(expected = 32, actual = loaded!!.width)
        assert(expected = 16, actual = loaded.height)
    }

    @Test
    fun resizeImageShrinksLargeImage() {
        val file = tempImageFile()
        file.write(newBitmap(width = 64, height = 64))
        file.resizeImage(maxTargetWidth = 16, maxTargetHeight = 16)
        val resized = file.loadBitmap()
        assertNotNull(resized)
        assert(expected = 16, actual = resized!!.width)
        assert(expected = 16, actual = resized.height)
    }
}
