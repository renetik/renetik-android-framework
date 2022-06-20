package renetik.android.core.lang

import android.app.Application
import renetik.android.core.BuildConfig
import renetik.android.core.kotlin.createClass
import renetik.android.core.kotlin.invokeFunction

object CSEnvironment {
    var isDebug = BuildConfig.DEBUG

    val application: Application?
        get() = createClass<Any>("android.app.ActivityThread")
            ?.invokeFunction("currentApplication") as? Application
}