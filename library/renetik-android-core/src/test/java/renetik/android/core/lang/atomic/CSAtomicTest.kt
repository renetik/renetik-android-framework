package renetik.android.core.lang.atomic

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import renetik.android.core.lang.atomic.CSAtomic.Companion.atomic
import renetik.android.core.lang.atomic.CSNullableAtomic.Companion.nullableAtomic

class CSAtomicTest {

    @Test
    fun atomicDelegateStoresLatestValue() {
        val holder = AtomicHolder()

        assertEquals("initial", holder.value)
        holder.value = "changed"
        assertEquals("changed", holder.value)
    }

    @Test
    fun nullableAtomicDelegateStoresNullAndLatestValue() {
        val holder = AtomicHolder()

        assertNull(holder.optional)
        holder.optional = "present"
        assertEquals("present", holder.optional)
        holder.optional = null
        assertNull(holder.optional)
    }

    private class AtomicHolder {
        var value by atomic("initial")
        var optional by nullableAtomic<String>()
    }
}
