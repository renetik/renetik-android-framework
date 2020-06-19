package renetik.android.sample.model

import com.google.firebase.crashlytics.FirebaseCrashlytics
import renetik.android.base.CSApplication
import renetik.android.base.application
import renetik.android.json.extensions.load
import renetik.android.logging.AndroidLogger
import renetik.android.sample.BuildConfig.DEBUG

val model by lazy { application.store.load(SampleModel::class, MODEL_KEY) }

class SampleApplication : CSApplication() {
    override val isDebugBuild = DEBUG
    override val logger by lazy {
        AndroidLogger().apply {
            onLogEvent.add { _, event ->
                FirebaseCrashlytics.getInstance().log("${event.type.title}: ${event.message}")
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
    }
}