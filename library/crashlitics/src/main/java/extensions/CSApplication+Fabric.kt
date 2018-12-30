package renetik.android.crashlitics.extensions

import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.core.CrashlyticsCore.Builder
import io.fabric.sdk.android.Fabric
import renetik.android.base.CSApplication

fun CSApplication.startFabricCrashlitics(): Crashlytics {
    val crashlytics = Crashlytics.Builder().core(Builder().disabled(isDebugBuild).build()).build()
    Fabric.with(this, crashlytics)
    return crashlytics
}

fun CSApplication.startFabricAnswers(): Answers {
    val answers = Answers()
    if (isDebugBuild) Fabric.with(this, answers)
    return answers
}

