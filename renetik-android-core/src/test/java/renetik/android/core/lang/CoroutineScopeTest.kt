package renetik.android.core.lang

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
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
import renetik.android.core.lang.result.createMainScope
import renetik.android.core.lang.result.mainScope
import renetik.android.core.logging.CSLog.logInfo
import renetik.android.testing.CSAssert.assert
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(application = CSTestApplication::class)
class CoroutineScopeTest {

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        mainScope = createMainScope()
    }

    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun cancellationTest() = runTest {
        val channel = Channel<Unit>(capacity = CONFLATED)
        var isLaunched = false
        var isCanceled = false
        val job = mainScope.launch {
            try {
                isLaunched = true
                for (signal in channel) {
                    logInfo()
                }
            } catch (ex: CancellationException) {
                isCanceled = true
            }
        }
        advanceTimeBy(6.milliseconds)
        assert(isLaunched)
        mainScope.cancel()
        advanceTimeBy(6.milliseconds)
        assert(isCanceled)
        job.cancel()
    }
}
