@file:OptIn(ExperimentalCoroutinesApi::class)

package renetik.android.event.dispatch


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
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
import renetik.android.event.dispatch.CSDebouncer.Companion.debouncer
import renetik.android.event.lifecycle.CSModel
import renetik.android.event.lifecycle.destruct
import renetik.android.testing.CSAssert.assert

@RunWith(RobolectricTestRunner::class)
@Config(application = CSTestApplication::class)
class CSLaterOnceTest {

    @Before
    fun setUp() = Dispatchers.setMain(StandardTestDispatcher())

    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun laterOnceTest() = runTest {
        var count = 0
        val model = CSModel()
        val laterOnceFunc = model.debouncer { count++ }

        laterOnceFunc()
        laterOnceFunc()
        advanceUntilIdle()
        assert(expected = 1, count)

        laterOnceFunc()
        advanceUntilIdle()
        laterOnceFunc()
        model.destruct()
        advanceUntilIdle()
        assert(expected = 2, count)
    }
}
