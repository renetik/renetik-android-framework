package renetik.android.core.android.app

import android.app.Application
import android.content.Intent.makeRestartActivityTask
import renetik.android.core.lang.CSLang.ExitStatus.OK
import renetik.android.core.lang.CSLang.exit
import renetik.android.core.logging.CSLog.logInfo
import renetik.android.core.logging.CSLog.logWarn

@Deprecated("Unreliable")
fun Application.exitStart() {
    logInfo("Application Restart")
    val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
    if (launchIntent == null) {
        logWarn("getLaunchIntentForPackage returned null")
        exit(OK)
    } else {
        val intent = makeRestartActivityTask(launchIntent.component)
        startActivity(intent)
        exit(OK)
    }
}