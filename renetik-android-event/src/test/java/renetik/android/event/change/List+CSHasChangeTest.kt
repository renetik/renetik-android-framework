package renetik.android.event.change


import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.core.lang.variable.increment
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.invoke
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.registration.paused
import renetik.android.testing.CSAssert.assert

@RunWith(RobolectricTestRunner::class)
class ListCSHasChangeTest {

    @Test
    fun listOnChange() {
        val property1 = property(1)
        val list: List<CSHasChange<*>> = listOf(property1, property(2))
        var onChangeFired = 0
        val registration = list.onChange { onChangeFired++ }
        property1.increment()
        assert(expected = 2, actual = property1.value)
        assert(expected = 1, actual = onChangeFired)
        registration.pause()
        property1.increment()
        assert(expected = 3, actual = property1.value)
        assert(expected = 1, actual = onChangeFired)
        registration.resume()
        assert(expected = 3, actual = property1.value)
        assert(expected = 1, actual = onChangeFired)
    }

    @Test
    fun listOnChange2() {
        val event1 = event()
        val event2 = event()
        val list: List<CSHasChange<Unit>> = listOf(event1, event2)
        var onChangeFired = 0
        val registration = list.onChange { onChangeFired++ }
        event1()
        assert(expected = 1, actual = onChangeFired)
        event2()
        assert(expected = 2, actual = onChangeFired)
        registration.paused { event1() }
        assert(expected = 2, actual = onChangeFired)
        event1()
        assert(expected = 3, actual = onChangeFired)
    }
}
