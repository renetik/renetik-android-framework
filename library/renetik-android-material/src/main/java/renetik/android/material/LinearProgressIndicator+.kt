package renetik.android.material

import com.google.android.material.progressindicator.LinearProgressIndicator

fun LinearProgressIndicator.onProgress(progress: Int) = setProgressCompat(progress, true)