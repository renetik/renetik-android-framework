package renetik.android.core.leakcanary

import android.net.Uri
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import renetik.android.core.lang.CSLeakCanary
import renetik.android.testing.TestApplication

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class CSLeakCanaryProviderTest {

    @Test
    fun onCreateInstallsImplementation() {
        val provider = CSLeakCanaryProvider()
        assertTrue(provider.onCreate())
        // facade now delegates to the real implementation
        assertTrue(CSLeakCanary.isEnabled)
        CSLeakCanary.disable()
        assertTrue(!CSLeakCanary.isEnabled)
        CSLeakCanary.enable()
        assertTrue(CSLeakCanary.isEnabled)
    }

    @Test
    fun contentProviderContractIsNoOp() {
        val provider = CSLeakCanaryProvider()
        val uri = Uri.parse("content://renetik.leakcanary/test")
        assertNull(provider.query(uri, null, null, null, null))
        assertNull(provider.getType(uri))
        assertNull(provider.insert(uri, null))
        assertTrue(provider.delete(uri, null, null) == 0)
        assertTrue(provider.update(uri, null, null, null) == 0)
    }

    @Test
    fun expectWeaklyReachableIsSafeUnderTestRunner() {
        CSLeakCanaryProvider().onCreate()
        with(CSLeakCanary) {
            Any().expectWeaklyReachable { "no-op in tests" }
        }
    }
}
