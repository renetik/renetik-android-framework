package renetik.android.core.lang.atomic

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotSame
import org.junit.Test

class CSProducerConsumerDataTest {

    @Test
    fun swapPromotesPreparedDataAndRecyclesOldActiveData() {
        var created = 0
        val data = CSProducerConsumerData { Buffer(++created) }
        val initialActive = data.active
        val initialPrepared = data.prepared
        val prepared = Buffer(10)

        data.prepare(prepared)
        data.swap()

        assertEquals(prepared, data.active)
        assertEquals(initialActive, data.prepared)
        assertNotSame(initialPrepared, data.prepared)
    }

    @Test
    fun clearRecreatesBothBuffers() {
        var created = 0
        val data = CSProducerConsumerData { Buffer(++created) }

        data.prepare(Buffer(20))
        data.swap()
        data.clear()

        assertEquals(Buffer(3), data.active)
        assertEquals(Buffer(4), data.prepared)
    }

    private data class Buffer(val id: Int)
}
