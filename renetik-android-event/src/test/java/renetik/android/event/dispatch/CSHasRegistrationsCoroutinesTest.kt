package renetik.android.event.dispatch


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import renetik.android.core.base.CSTestApplication
import renetik.android.core.lang.result.invoke
import renetik.android.core.lang.variable.setFalse
import renetik.android.core.lang.variable.setTrue
import renetik.android.event.change.onChangeLaunch
import renetik.android.event.change.waitForFalse
import renetik.android.event.lifecycle.CSModel
import renetik.android.event.lifecycle.destruct
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.property.CSSafePropertyImpl.Companion.safeProperty
import renetik.android.event.registration.plus
import renetik.android.testing.CSAssert.assert
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(application = CSTestApplication::class)
class CSHasRegistrationsCoroutinesTest {

    @Before
    fun setUp() = Dispatchers.setMain(StandardTestDispatcher())

    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun launch() = runTest {
        val model = CSModel()
        var count = 0
        fun launch() = model.launch { count++ }
        launch()
        launch()
        advanceUntilIdle()
        assert(2, count)
        launch()
        model.destruct()
        advanceUntilIdle()
        assert(2, count)
    }

    @Test
    fun launchIfNot() = runTest {
        val model = CSModel()
        var value = 2
        model.launchIfNot("launchIfNot") { value = 2 }
        model.launchIfNot("launchIfNot") { value = 4 }
        advanceUntilIdle()
        assert(0, model.registrations.size)
        assert(2, value)
    }

    @Test
    fun launchReplace() = runTest {
        val model = CSModel()
        assert(0, model.registrations.size)
        var count = 0
        var value = 2
        model.launch("replace") {
            count += 1
            value = 2
        }
        model.launch("replace") {
            count += 1
            value = 4
        }
        advanceUntilIdle()
        assert(0, model.registrations.size)
        assert(1, count)
        assert(4, value)
    }

    @Test
    fun launchFromLaunch() = runTest {
        val model = CSModel()
        var count = 0
        var value = 2
        model.launch {
            count += 1
            value = 2
            Main.launch {
                count += 1
                value = 4
            }
        }
        advanceUntilIdle()
        assert(0, model.registrations.size)
        assert(2, count)
        assert(4, value)
    }


    @Test
    fun launchFromLaunchCancel() = runTest {
        val wait1 = property(true)
        val wait2 = property(true)
        val model = CSModel()
        var count = 0
        model.launch {
            count += 1
            wait1.waitForFalse()
            Main {
                count += 1
                wait2.waitForFalse()
                Main.launch {
                    count += 1
                }
            }
        }
        advanceTimeBy(30)
        assert(1, model.registrations.size)
        assert(1, count)
        wait1.setFalse()
        advanceUntilIdle()
        assert(1, model.registrations.size)
        assert(2, count)
        model.destruct()
        wait2.setFalse()
        advanceUntilIdle()
        assert(0, model.registrations.size)
        assert(2, count)
    }

    @Test
    fun contextFromLaunchCancel() = runTest {
        val parent = CSModel()
        val wait1 = parent.safeProperty(true)
        val wait2 = parent.safeProperty(true)
        var count = 0
        val registration = parent.launch {
            count += 1
            wait1.waitForFalse()
            Main {
                count += 1
                wait2.waitForFalse()
                Main {
                    count += 1
                    assert(3, count)
                }
            }
        }
        advanceUntilIdle()
        assert(1, count)
        wait1.setFalse()
        advanceUntilIdle()
        assert(2, count)
        registration.cancel()
        wait2.setFalse()
        advanceUntilIdle()
        assert(2, count)
    }


    @Test
    fun testRegistrationKeyLaunchCancel() = runTest {
        val parent = CSModel()
        assert(0, parent.registrations.size)
        var count = 0

        parent.launch("test key") { delay(5.milliseconds); count++ }
        parent.launch("test key") { delay(5.milliseconds); count++ }
        parent.launch("test key") { delay(5.milliseconds); count++ }
        assert(1, parent.registrations.size)

        advanceUntilIdle()
        assert(1, count)
        assert(0, parent.registrations.size)
    }

    @Test
    fun testOnChangeLaunchCancelParentDestruct() = runTest {
        val parent = CSModel()
        val property = property(false)
        assert(0, parent.registrations.size)
        var count = 0

        parent + property.onChangeLaunch { delay(5.milliseconds); count++ }
        assert(1, parent.registrations.size)

        property.setTrue(); property.setFalse()
        property.setTrue(); property.setFalse()
        parent.destruct()

        advanceUntilIdle()
        assert(0, count)
        assert(0, parent.registrations.size)
    }

    @Test
    fun testOnChangeKeyLaunchCancel() = runTest {
        val property = property(false)
        var count = 0
        property.onChangeLaunch("key") { delay(5.milliseconds); count++ }
        property.setTrue(); property.setFalse()
        property.setTrue(); property.setFalse()
        advanceUntilIdle()
        assert(1, count)
    }
}
