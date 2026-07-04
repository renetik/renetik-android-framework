package renetik.android.preset.property.nullable

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.store.type.CSJsonObjectStore

@RunWith(RobolectricTestRunner::class)
class CSIntNullablePresetEventPropertyTest {

    private val store = CSJsonObjectStore()
    private val parent = CSPresetTestParentClass(store)

    @Test
    fun test() {
        assertEquals(1, parent.property1.value)
        parent.property1.value = 2
        assertEquals(2, parent.property1.value)
        parent.property1.value = null
        assertEquals(null, parent.property1.value)
        parent.parentPreset.reloadInternal()
        assertEquals(1, parent.property1.value)
    }

    @Test
    fun test2() {
        parent.property1.value = null
        parent.property2.value = 3
        assertEquals(null, parent.property1.value)
        assertEquals(3, parent.property2.value)
    }
}

