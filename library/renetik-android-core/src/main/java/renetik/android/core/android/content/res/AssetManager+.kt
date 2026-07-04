package renetik.android.core.android.content.res

import android.content.res.AssetManager
import renetik.android.core.java.io.deleteAll
import renetik.android.core.kotlin.unexpected
import renetik.android.core.logging.CSLog.logError
import java.io.File

fun AssetManager.copyPathToDir(
    item: String, targetDir: File,
    overwrite: Boolean = true
) {
    val item = item.trimEnd('/')
    if (targetDir.exists() && targetDir.isFile) {
        logError("targetDir:$targetDir is file")
        unexpected()
    }
    targetDir.mkdirs()
    if (isFile(item)) {
        val name = item.substringAfterLast('/')
        val dest = File(targetDir, name)
        if (overwrite || !dest.exists()) copyFile(item, dest)
        return
    }
    list(item)?.forEach { name ->
        val path = if (item.isEmpty()) name else "$item/$name"
        val dest = File(targetDir, name)
        if (isDir(path)) {
            if (overwrite || !dest.exists()) dest.mkdirs()
            copyPathToDir(path, dest, overwrite)
        } else if (overwrite || !dest.exists()) copyFile(path, dest)
    }
}

private fun AssetManager.copyFile(path: String, dest: File) {
    if (dest.exists()) {
        dest.deleteAll()
        if (dest.exists()) logError("Failed to delete $dest")
    }
    dest.parentFile?.mkdirs()
    open(path).use { input -> dest.outputStream().use(input::copyTo) }
}

fun AssetManager.isFile(path: String): Boolean =
    path.isNotEmpty() &&
            runCatching { open(path).close(); true }.getOrDefault(false)

fun AssetManager.isDir(path: String): Boolean =
    runCatching { list(path)?.isNotEmpty() == true }.getOrDefault(false)