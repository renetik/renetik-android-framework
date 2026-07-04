package renetik.android.core.android.graphics

import android.graphics.Color
import android.graphics.Color.alpha
import android.graphics.Color.blue
import android.graphics.Color.green
import android.graphics.Color.red
import android.graphics.Paint.Style.STROKE
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.core.android.graphics.CSColor.Companion.colorRes
import renetik.android.testing.context

@RunWith(RobolectricTestRunner::class)
class GraphicsExtensionsTest {

    @Test
    fun colorHexIdAndGeneratedRangesAreStable() {
        val color = CSColor("#123456")

        assertEquals(Color.rgb(0x12, 0x34, 0x56), color.color)
        assertEquals("#123456", color.toHex())
        assertEquals("#123456", color.id)
        assertEquals("#FF0000", with(CSColor.Companion) { Color.RED.toHex() })
        assertEquals(12, CSColor.standard.size)

        CSColor.generateLightColors(20).forEach {
            assertTrue(red(it) in 200..254)
            assertTrue(green(it) in 200..254)
            assertTrue(blue(it) in 200..254)
        }
        CSColor.generateDarkColors(20).forEach {
            assertTrue(red(it) in 0..54)
            assertTrue(green(it) in 0..54)
            assertTrue(blue(it) in 0..54)
        }
    }

    @Test
    fun contextColorHelpersAndAlphaOperations() {
        val colorId = resource("test_color", "color")

        assertEquals("#123456", context.colorRes(colorId).toHex())
        assertEquals(
            "#445566",
            with(CSColor.Companion) { context.colorInt(Color.rgb(0x44, 0x55, 0x66)).toHex() }
        )
        assertEquals(127, 0.5f.alphaInt)
        assertEquals(127, alpha(Color.RED.setAlpha(0.5f)))
        assertEquals(255, alpha(Color.RED.setAlpha(1f)))
        assertThrows(IllegalArgumentException::class.java) { Color.RED.setAlpha(-0.1f) }
        assertNotEquals(Color.RED, Color.RED.darken)
        assertNotEquals(Color.RED, Color.RED.darkenMore)
    }

    @Test
    fun rectLoadingOffsetsCopyAndContainsUseExpectedGeometry() {
        val rect = Rect().load(
            renetik.android.core.math.CSPoint(10f, 20f),
            renetik.android.core.math.CSPoint(2f, 5f)
        )

        assertEquals(Rect(2, 5, 10, 20), rect)
        assertEquals(8, rect.width)
        assertEquals(15, rect.height)
        assertEquals("left:2 top:5 width:8 height:15", rect.debugString)
        assertTrue(rect.contains(Point(3, 6)))

        rect.offsetToNewRight(30)
        assertEquals(Rect(22, 5, 30, 20), rect)
        rect.offsetToNewBottom(40)
        assertEquals(Rect(22, 25, 30, 40), rect)
        assertEquals(rect, rect.copy())
        rect.clear()
        assertEquals(Rect(0, 0, 0, 0), rect)
        assertFalse((null as Rect?).contains(Point(0, 0)))
    }

    @Test
    fun paintFactoryCloneAndDrawableAlphaKeepExpectedValues() {
        val paint = Paint {
            color = Color.RED
            strokeWidth = 3f
            style = STROKE
        }

        val clone = paint.clone {
            color = Color.BLUE
        }

        assertTrue(paint.isAntiAlias)
        assertEquals(Color.RED, paint.color)
        assertEquals(Color.BLUE, clone.color)
        assertEquals(3f, clone.strokeWidth)
        assertEquals(STROKE, clone.style)

        val alphaDrawable = ColorDrawable(Color.RED).toAlpha(0.5) as ColorDrawable
        assertEquals(128, alpha(alphaDrawable.color))
    }

    private fun resource(name: String, type: String): Int =
        context.resources.getIdentifier(name, type, context.packageName).also {
            assertTrue("$type/$name should exist", it != 0)
        }
}
