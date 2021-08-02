package renetik.android.framework

import android.app.Application
import android.os.Environment.getExternalStorageDirectory
import renetik.android.content.applicationLabel
import renetik.android.framework.store.CSPreferencesStore
import renetik.android.java.extensions.exception
import renetik.android.logging.AndroidLogger
import renetik.android.logging.CSLog.logInfo
import renetik.android.logging.CSLog.logWarn
import renetik.android.logging.CSLogger
import java.io.File

open class CSApplication : Application() {
    companion object {
        lateinit var application: CSApplication
    }

    open val name: String by lazy { applicationLabel }
    open val logger: CSLogger by lazy { AndroidLogger() }
    open val store: CSPreferencesStore by lazy { CSPreferencesStore("ApplicationSettings") }
    open val externalFilesDir: File
        get() = getExternalFilesDir(null) ?: getExternalStorageDirectory()
    open val isDebugBuild: Boolean
        get() {
            throw exception("You need to override this if like to use it in your implementation of CSApplication," +
                    " because BuildConfig.DEBUG returns true only in debugged module")
        }
    open val isDevelopmentMode = isDebugBuild

    override fun onCreate() {
        super.onCreate()
        application = this
    }

    override fun onLowMemory() {
        super.onLowMemory()
        logWarn("onLowMemory")
        logger.onLowMemory()
    }

    override fun onTerminate() {
        super.onTerminate()
        logInfo("onTerminate")
    }
}
