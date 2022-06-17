package renetik.android.core

import android.app.Application
import android.content.Context
import android.os.Environment.getExternalStorageDirectory
import renetik.android.core.extensions.content.applicationLabel
import renetik.android.core.logging.AndroidLogger
import renetik.android.core.logging.CSLog.logInfo
import renetik.android.core.logging.CSLog.logWarn
import renetik.android.core.logging.CSLogger
import java.io.File

abstract class CSApplication : Application() {
    companion object {
        lateinit var app: CSApplication
    }

    open val name: String by lazy { applicationLabel }
    open val log: CSLogger by lazy { AndroidLogger() }

    open val externalFilesDir: File
        get() = getExternalFilesDir(null) ?: getExternalStorageDirectory()

    abstract val isDebugBuild: Boolean
    open val isDevelopmentMode get() = isDebugBuild

//    lateinit var store: CSStore
//    override fun attachBaseContext(context: Context) {
//        store = CSFileJsonStore(context, "app", isJsonPretty = true)
//        super.attachBaseContext(onAttachBaseContext(context))
//    }

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