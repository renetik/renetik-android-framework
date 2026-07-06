package renetik.android.event.property


import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.core.lang.variable.assign
import renetik.android.event.lifecycle.CSModel
import renetik.android.event.lifecycle.destruct
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.property.CSSafeProperty.Companion.safe
import renetik.android.testing.CSAssert.assert

@RunWith(RobolectricTestRunner::class)
class CSSafePropertyTest {

    @Test
    fun simpleTest() {
        val parent = CSModel()
        val property = property<Boolean>()
        val safeProperty: CSSafeProperty<Boolean?> = property.safe(parent)
        assert(expected = null, property.value)
        assert(expected = null, safeProperty.value)
        property assign true
        assert(expected = true, property.value)
        assert(expected = true, safeProperty.value)
        safeProperty assign false
        assert(expected = false, property.value)
        assert(expected = false, safeProperty.value)

        parent.destruct()
        property assign true
        assert(expected = true, property.value)
        assert(expected = false, safeProperty.value)
    }
}