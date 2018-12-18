package renetik.android.model

import android.app.Application
import android.os.Environment.getExternalStorageDirectory
import renetik.android.lang.CSLog.logInfo
import renetik.android.lang.CSLogger
import renetik.android.lang.initializeHandler
import java.io.File

lateinit var application: CSApplication

abstract class CSApplication : Application() {

    abstract val name: String
    abstract val logger: CSLogger
    open val store: CSValueStore by lazy { CSValueStore("ApplicationSettings") }
    abstract val isDebugBuild: Boolean
    val externalFilesDir: File get() = getExternalFilesDir(null) ?: getExternalStorageDirectory()

    override fun onCreate() {
        super.onCreate()
        initializeHandler()
        application = this
    }

    override fun onLowMemory() {
        super.onLowMemory()
        logInfo("onLowMemory")
        logger.onLowMemory()
    }

    override fun onTerminate() {
        super.onTerminate()
        logInfo("onTerminate")
    }

}
