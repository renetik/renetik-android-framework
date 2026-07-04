package renetik.android.core.java.io

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

@RunWith(RobolectricTestRunner::class)
class FileExtensionsTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    @Test
    fun writeReadAndRecreateFile() {
        val file = File(tempFolder.root, "nested/value.txt")

        file.write("first")
        assertTrue(file.exists())
        assertEquals("first", file.readString())

        file.recreateFile()
        assertTrue(file.exists())
        assertEquals("", file.readText())
        assertEquals(null, File(tempFolder.root, "missing.txt").readString())
    }

    @Test
    fun directoryListingsAndCountsSeparateFilesAndDirs() {
        val root = tempFolder.newFolder("items")
        File(root, "a.txt").writeText("a")
        File(root, "b.txt").writeText("b")
        File(root, "dir").mkdir()

        val visitedFiles = mutableListOf<String>()
        val visitedDirs = mutableListOf<String>()
        root.forEachFile { visitedFiles += it.name }
        root.forEachDir { visitedDirs += it.name }

        assertEquals(listOf("a.txt", "b.txt"), root.files().map(File::getName).sorted())
        assertEquals(listOf("dir"), root.dirs().map(File::getName))
        assertEquals(listOf("a.txt", "b.txt"), visitedFiles.sorted())
        assertEquals(listOf("dir"), visitedDirs)
        assertEquals(3, root.itemCount)
        assertFalse(root.isDirEmpty)
    }

    @Test
    fun writeAtomicReplacesContentAndRemovesTemporaryFile() {
        val file = File(tempFolder.root, "atomic.txt")

        file.writeAtomic("one")
        file.writeAtomic("two")

        assertEquals("two", file.readText())
        assertFalse(File(tempFolder.root, "atomic.txt.tmp").exists())
    }

    @Test
    fun atomicMoveAndCopyReportMissingFiles() {
        val source = File(tempFolder.root, "source.txt").write("source")
        val target = File(tempFolder.root, "target.txt").write("target")
        val copyTarget = File(tempFolder.root, "copy.txt")

        assertTrue(source.copy(copyTarget))
        assertEquals("source", copyTarget.readText())
        assertTrue(source.atomicMove(target))
        assertFalse(source.exists())
        assertEquals("source", target.readText())
        assertFalse(File(tempFolder.root, "missing.txt").copy(copyTarget))
        assertFalse(File(tempFolder.root, "missing.txt").atomicMove(target))
    }

    @Test
    fun fileListHonorsDepth() {
        val root = tempFolder.newFolder("tree")
        File(root, "root.txt").writeText("root")
        File(root, "dir").mkdir()
        File(root, "dir/child.txt").writeText("child")
        File(root, "dir/sub").mkdir()
        File(root, "dir/sub/grandchild.txt").writeText("grandchild")

        assertEquals(listOf("root.txt"), root.fileList(depth = 0).relativeNames(root))
        assertEquals(listOf("dir/child.txt", "root.txt"), root.fileList(depth = 1).relativeNames(root))
        assertEquals(
            listOf("dir/child.txt", "dir/sub/grandchild.txt", "root.txt"),
            root.fileList(depth = 2).relativeNames(root)
        )
    }

    @Test
    fun createDatedFileWithAndDeleteHelpers() {
        val dir = File(tempFolder.root, "dated")
        val file = dir.createDatedFile("json")
        val named = file.with(name = "renamed")
        val typed = file.with(extension = "txt")

        assertTrue(dir.exists())
        assertTrue(file.exists())
        assertEquals("json", file.extension)
        assertEquals("renamed.json", named.name)
        assertEquals("${file.nameWithoutExtension}.txt", typed.name)
        assertTrue(file.isJson)

        dir.deleteAll()
        assertFalse(dir.exists())
        dir.safeDelete()
    }

    private fun List<File>.relativeNames(root: File): List<String> =
        map { it.relativeTo(root).path }.sorted()
}
