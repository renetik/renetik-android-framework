package renetik.android.extensions

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import renetik.android.CSApplicationMock
import renetik.android.base.CSApplicationInstance.application

@RunWith(RobolectricTestRunner::class)
@Config(manifest = "AndroidManifest.xml", application = CSApplicationMock::class)
class ContextTest {
    @Test
    fun applicationNameTest() {
        assertEquals("CSApplicationMock", application.name)
        assertEquals("CSApplicationMock", application.applicationLabel)
        assertNotNull(application.applicationLogo)
        assertNotNull(application.applicationIcon)
    }
}


