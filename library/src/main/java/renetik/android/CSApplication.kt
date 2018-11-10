package renetik.android

import android.app.Application
import renetik.android.lang.CSDoLater
import renetik.android.lang.CSLang.info
import renetik.android.lang.CSLogger
import renetik.android.model.CSValueStore

lateinit var application: CSApplication

abstract class CSApplication : Application() {

    abstract val name: String
    abstract val logger: CSLogger
    open val store: CSValueStore by lazy { CSValueStore("ApplicationSettings") }
    abstract val isDebugBuild: Boolean

    override fun onCreate() {
        super.onCreate()
        CSDoLater.initializeHandler()
        application = this
    }

    override fun onLowMemory() {
        super.onLowMemory()
        info("onLowMemory")
        logger.onLowMemory()
    }

    override fun onTerminate() {
        super.onTerminate()
        info("onTerminate")
    }

}
