package renetik.android.framework

import android.app.Application
import android.os.Environment.getExternalStorageDirectory
import renetik.android.content.applicationLabel
import renetik.android.framework.json.store.CSFileJsonStore
import renetik.android.framework.logging.AndroidLogger
import renetik.android.framework.logging.CSLog.info
import renetik.android.framework.logging.CSLog.warn
import renetik.android.framework.logging.CSLogger
import renetik.android.framework.store.CSStoreInterface
import renetik.kotlin.exception
import java.io.File

open class CSApplication : Application() {
    companion object {
        lateinit var application: CSApplication
    }

    open val name: String by lazy { applicationLabel }
    open val log: CSLogger by lazy { AndroidLogger() }
    open val store: CSStoreInterface by lazy { CSFileJsonStore("store", isJsonPretty = true) }
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
        warn("onLowMemory")
    }

    override fun onTerminate() {
        super.onTerminate()
        info("onTerminate")
    }
}
