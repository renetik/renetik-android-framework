package renetik.android.content.res

import android.content.res.AssetManager
import renetik.android.java.extensions.copy
import renetik.android.java.extensions.createFileAndDirs
import renetik.android.primitives.iterator
import java.io.File

fun AssetManager.copyFilesToDir(path: String, targetDir: File, overwrite: Boolean = false) {
    for (file in list(path).iterator) {
        val target = File(targetDir, file)
        if (overwrite || !target.exists())
            target.createFileAndDirs().outputStream().use {
                open("$path/$file".replace("//", "/")).copy(it)
            }
    }
}