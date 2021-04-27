package renetik.android

import renetik.android.framework.CSApplication

class CSApplicationMock : CSApplication() {
    override val isDebugBuild get() = BuildConfig.DEBUG
}