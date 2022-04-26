package renetik.android.app

import android.app.Application
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Process
import kotlin.system.exitProcess

fun Application.restart() {
    val intent = packageManager.getLaunchIntentForPackage(packageName)!!
    intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    Process.killProcess(Process.myPid())
    exitProcess(0)
}