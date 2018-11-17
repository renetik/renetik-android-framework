package renetik.android.viewbase

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import renetik.android.java.collections.CSMap
import renetik.android.lang.CSLang

fun CSViewController<*>.startActivity(activityClass: Class<out AppCompatActivity>) {
    startActivity(Intent(activity(), activityClass))
}

fun CSViewController<*>.startActivity(activityClass: Class<out AppCompatActivity>, extras: CSMap<String, String>) {
    val intent = Intent(activity(), activityClass)
    for ((key, value) in extras)
        intent.putExtra(key, value)
    startActivity(intent)
}

fun CSViewController<*>.startActivityForResult(activityClass: Class<out AppCompatActivity>, requestCode: Int) =
        startActivityForResult(Intent(activity(), activityClass), requestCode)

fun CSViewController<*>.startActivityForResult(intent: Intent, requestCode: Int) =
        activity().startActivityForResult(intent, requestCode)

fun CSViewController<*>.switchActivity(activityClass: Class<out AppCompatActivity>) =
        switchActivity(Intent(activity(), activityClass))

fun CSViewController<*>.switchActivity(intent: Intent) {
    activity().finish()
    startActivity(intent)
}

fun CSViewController<*>.switchActivity(activityClass: Class<out AppCompatActivity>, resultCode: Int) {
    activity().setResult(resultCode)
    switchActivity(Intent(activity(), activityClass))
}

fun CSViewController<*>.restartActivity() {
    CSLang.doLater({
        val intent = activity().intent
        activity().finish()
        startActivity(intent)
    })
}

fun CSViewController<*>.goHome() =
        startActivity(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME))

fun CSViewController<*>.startApplication(packageName: String) {
    try {
        val intent = Intent("android.intent.action.MAIN")
        intent.addCategory("android.intent.category.LAUNCHER")
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        val resolveInfoList = activity().packageManager.queryIntentActivities(intent, 0)
        for (info in resolveInfoList)
            if (info.activityInfo.packageName.equals(packageName, ignoreCase = true)) {
                launchComponent(info.activityInfo.packageName, info.activityInfo.name)
                return
            }
        showInMarket(packageName)
    } catch (e: Exception) {
        showInMarket(packageName)
    }
}

private fun CSViewController<*>.launchComponent(packageName: String, name: String) {
    val intent = Intent("android.intent.action.MAIN")
    intent.addCategory("android.intent.category.LAUNCHER")
    intent.component = ComponentName(packageName, name)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}

private fun CSViewController<*>.showInMarket(packageName: String?) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName!!))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}