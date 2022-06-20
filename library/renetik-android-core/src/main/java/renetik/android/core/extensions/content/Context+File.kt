package renetik.android.core.extensions.content

import android.content.Context
import java.io.File

fun Context.createTempFile() = File.createTempFile(applicationLabel, null, cacheDir)