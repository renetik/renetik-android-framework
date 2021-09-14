package renetik.android.framework.util

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.os.Debug
import android.text.format.Formatter.formatFileSize
import renetik.android.framework.CSApplication.Companion.application

// https://stackoverflow.com/questions/3170691/how-to-get-current-memory-usage-in-android
// The more the "usedMemInMB" variable gets close to "maxHeapSizeInMB",
// the closer availHeapSizeInMB gets to zero, the closer you get OOM.
// (Due to memory fragmentation, you may get OOM BEFORE this reaches zero.)
object CSMemory {
    fun Runtime.getAvailableHeapSizeInMB(): Long {
        val usedMemInMB = (totalMemory() - freeMemory()) / 1048576L
        val maxHeapSizeInMB = maxMemory() / 1048576L
        return maxHeapSizeInMB - usedMemInMB
    }

    fun usedMemoryInPercent(): Long {
        val nativeHeapSize = Debug.getNativeHeapSize()
        val nativeHeapFreeSize = Debug.getNativeHeapFreeSize()
        val usedMemInBytes = nativeHeapSize - nativeHeapFreeSize
        return usedMemInBytes * 100 / nativeHeapSize
    }

    data class RealTotalMemoryUsage(val nativeHeapSize: Long, val nativeHeapFreeSize: Long) {
        private val usedMemInBytes get() = nativeHeapSize - nativeHeapFreeSize
        private val usedMemInPercentage = usedMemInBytes * 100 / nativeHeapSize
        override fun toString() =
            "Real memory usage total:${formatFileSize(application, nativeHeapSize)} " +
                    ",free:${formatFileSize(application, nativeHeapFreeSize)} " +
                    ",used:${formatFileSize(application, usedMemInBytes)} ($usedMemInPercentage%)"
    }

    fun Context.getRealTotalMemoryUsage(): RealTotalMemoryUsage {
        val memoryInfo = ActivityManager.MemoryInfo()
        (getSystemService(ACTIVITY_SERVICE) as ActivityManager).getMemoryInfo(memoryInfo)
        return RealTotalMemoryUsage(memoryInfo.totalMem, memoryInfo.availMem)
    }
}