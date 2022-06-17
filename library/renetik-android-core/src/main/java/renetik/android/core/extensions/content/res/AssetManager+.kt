package renetik.android.core.extensions.content.res

import android.content.res.AssetManager
import renetik.android.core.kotlin.primitives.iterator
import renetik.android.core.java.io.copy
import renetik.android.core.java.io.createFileAndDirs
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

fun AssetManager.copyFileToDir(assetPath: String, folder: File, overwrite: Boolean = false) {
    val target = File(folder, File(assetPath).name)
    if (overwrite || !target.exists())
        target.createFileAndDirs().outputStream().use {
            open(assetPath.replace("//", "/")).copy(it)
        }
}