package renetik.android.sample.model

import renetik.android.framework.CSApplication
import renetik.android.framework.CSApplication.Companion.application
import renetik.android.json.extensions.load
import renetik.android.sample.BuildConfig.DEBUG

val model by lazy { application.store.load(SampleModel::class, MODEL_KEY) }

class SampleApplication : CSApplication() {
    override val isDebugBuild = DEBUG
}