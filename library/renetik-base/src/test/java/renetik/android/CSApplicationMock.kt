package renetik.android

import renetik.android.base.CSApplication

class CSApplicationMock : CSApplication() {
    override val isDebugBuild get() = BuildConfig.DEBUG
}