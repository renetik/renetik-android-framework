package renetik.android.core

import android.app.Application
import android.content.Context
import android.os.Environment.getExternalStorageDirectory
import renetik.android.core.logging.CSLog.logInfo
import renetik.android.core.logging.CSLog.logWarn
import java.io.File

abstract class CSApplication : Application() {
    companion object {
        lateinit var app: CSApplication
    }

    open val externalFilesDir: File
        get() = getExternalFilesDir(null) ?: getExternalStorageDirectory()

    protected open fun onAttachBaseContext(context: Context) = context

    override fun onCreate() {
        super.onCreate()
        app = this
    }

    override fun onLowMemory() {
        super.onLowMemory()
        logWarn("onLowMemory")
    }

    override fun onTerminate() {
        super.onTerminate()
        logInfo("onTerminate")
    }
}