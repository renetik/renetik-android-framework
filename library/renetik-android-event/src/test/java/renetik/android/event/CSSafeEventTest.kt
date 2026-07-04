package renetik.android.event

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLooper.runUiThreadTasksIncludingDelayedTasks
import renetik.android.event.CSSafeEvent.Companion.safeEmpty
import renetik.android.event.CSSafeEvent.Companion.safeEvent
import renetik.android.event.lifecycle.CSModel
import renetik.android.event.lifecycle.destruct

@RunWith(RobolectricTestRunner::class)
class CSSafeEventTest {

    @Test
    fun unsafeChangeIsImmediateAndChangeIsMainDispatched() {
        val parent = CSModel()
        val event = parent.safeEvent<Int>()
        var unsafeValue: Int? = null
        var changeValue: Int? = null
        event.onUnsafeChange { unsafeValue = it }
        event.onChange { changeValue = it }

        Thread { event.fire(7) }.apply {
            start()
            join()
        }

        assertEquals(7, unsafeValue)
        assertNull(changeValue)
        runUiThreadTasksIncludingDelayedTasks()
        assertEquals(7, changeValue)
    }

    @Test
    fun listenUsesMainDispatchedChangeChannel() {
        val parent = CSModel()
        val event = parent.safeEvent<Int>()
        var value: Int? = null
        event.listen { value = it }

        Thread { event.fire(3) }.apply {
            start()
            join()
        }

        assertNull(value)
        runUiThreadTasksIncludingDelayedTasks()
        assertEquals(3, value)
    }

    @Test
    fun pauseAndResumeAffectBothChannels() {
        val parent = CSModel()
        val event = parent.safeEvent<Unit>()
        var unsafeCount = 0
        var changeCount = 0
        event.onUnsafeChange { unsafeCount += 1 }
        event.onChange { changeCount += 1 }

        event.pause()
        event.fire()
        runUiThreadTasksIncludingDelayedTasks()
        assertEquals(0, unsafeCount)
        assertEquals(0, changeCount)

        event.resume()
        event.fire()
        assertEquals(1, unsafeCount)
        runUiThreadTasksIncludingDelayedTasks()
        assertEquals(1, changeCount)
    }

    @Test
    fun clearRemovesListenersAndUpdatesIsListened() {
        val parent = CSModel()
        val event = parent.safeEvent<Unit>()
        var unsafeCount = 0
        var changeCount = 0

        assertFalse(event.isListened)
        val unsafeRegistration = event.onUnsafeChange { unsafeCount += 1 }
        assertTrue(event.isListened)
        unsafeRegistration.cancel()
        assertFalse(event.isListened)

        event.onUnsafeChange { unsafeCount += 1 }
        event.onChange { changeCount += 1 }
        assertTrue(event.isListened)
        event.clear()
        assertFalse(event.isListened)

        event.fire()
        runUiThreadTasksIncludingDelayedTasks()
        assertEquals(0, unsafeCount)
        assertEquals(0, changeCount)
    }

    @Test
    fun parentDestructSkipsQueuedMainChange() {
        val parent = CSModel()
        val event = parent.safeEvent<Int>()
        var unsafeValue: Int? = null
        var changeValue: Int? = null
        event.onUnsafeChange { unsafeValue = it }
        event.onChange { changeValue = it }

        Thread { event.fire(5) }.apply {
            start()
            join()
        }
        parent.destruct()
        runUiThreadTasksIncludingDelayedTasks()

        assertEquals(5, unsafeValue)
        assertNull(changeValue)
    }

    @Test
    fun emptySafeEventIsNoOp() {
        val event = safeEmpty<Int>()
        var unsafeCount = 0
        var changeCount = 0

        event.onUnsafeChange { unsafeCount += 1 }
        event.onChange { changeCount += 1 }
        event.fire(1)

        assertFalse(event.isListened)
        assertEquals(0, unsafeCount)
        assertEquals(0, changeCount)
    }
}
