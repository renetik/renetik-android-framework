package renetik.android.sample.model

import renetik.android.base.CSApplication
import renetik.android.sample.BuildConfig.DEBUG

val model by lazy { SampleModel() }

class SampleApplication : CSApplication() {
    override val isDebugBuild = DEBUG
}