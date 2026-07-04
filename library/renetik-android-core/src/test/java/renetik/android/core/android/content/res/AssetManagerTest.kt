package renetik.android.core.android.content.res

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import renetik.android.testing.context
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class AssetManagerTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    @Test
    fun `copy single file`() {
        val target = tempFolder.newFolder("out1")
        // assume src/test/resources/assets/foo.txt exists
        context.assets.copyPathToDir("foo.txt", target)
        val dest = File(target, "foo.txt")
        assertTrue("foo.txt should exist", dest.exists())
        assertEquals("file contents should match", "Hello, Foo!\n", dest.readText())
    }

    @Test
    fun `copy directory recursively`() {
        val target = tempFolder.newFolder("out2")
        // assume assets/dir1/ contains a.txt and subdir/b.txt
        context.assets.copyPathToDir("dir1", target)
        val a = File(target, "a.txt")
        val b = File(target, "subdir/b.txt")
        assertTrue(a.exists())
        assertTrue(b.exists())
        assertEquals("A\n", a.readText())
        assertEquals("B\n", b.readText())
    }

    @Test
    fun `do not overwrite existing when overwrite=false`() {
        val target = tempFolder.newFolder("out3")
        // pre-create file with marker
        val pre = File(target, "foo.txt")
        pre.writeText("PREEXISTING")
        // copy with overwrite = false
        context.assets.copyPathToDir("foo.txt", target, overwrite = false)
        assertEquals("should not have overwritten", "PREEXISTING", pre.readText())
    }
}