package cs.android.view

import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.TextView
import cs.android.R
import cs.android.util.CSMail.sendMail
import cs.android.viewbase.CSViewController
import cs.java.lang.CSLang.list
import cs.java.lang.CSLang.model

class CSLogController(parent: CSViewController<*>) :
        CSViewController<View>(parent, layout(R.layout.cs_log)), CSNavigationItem {

    private val mailSubject =
            "${model().applicationName()} This is log from application send as email attachment for application developer";

    init {
        menu("Send to developer").onClick { onSendLogClick() }
    }

    override fun onCreate() {
        super.onCreate()
        loadText()
        item(R.id.CSLog_Reload).onClick { loadText() }
        view<TextView>(R.id.CSLog_LogText).asView().movementMethod = ScrollingMovementMethod()
    }

    private fun loadText() {
        item(R.id.CSLog_LogText).text(model().logger().logString())
    }

    private fun onSendLogClick() {
        sendMail(this, list("dohan.rene@gmail.com"), mailSubject, item(R.id.CSLog_LogText).text())
    }
}