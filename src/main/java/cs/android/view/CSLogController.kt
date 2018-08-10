package cs.android.view

import android.text.method.ScrollingMovementMethod
import android.view.View
import cs.android.R.id
import cs.android.R.layout
import cs.android.extensions.view.onClick
import cs.android.extensions.view.textView
import cs.android.extensions.view.title
import cs.android.extensions.view.view
import cs.android.util.CSMail.sendMail
import cs.android.viewbase.CSViewController
import cs.java.lang.CSLang.list
import cs.java.lang.CSLang.model

class CSLogController(parent: CSViewController<*>) :
        CSViewController<View>(parent, layout(layout.cs_log)), CSNavigationItem {

    private val mailSubject =
            "${model().applicationName()} This is log from application send as email attachment for application developer";

    init {
        menu("Send to developer").onClick { onSendLogClick() }
    }

    override fun onCreate() {
        super.onCreate()
        loadText()
        view(id.CSLog_Reload).onClick { loadText() }
        textView(id.CSLog_LogText).movementMethod = ScrollingMovementMethod()
    }

    private fun loadText() {
        textView(id.CSLog_LogText).title(model().logger().logString())
    }

    private fun onSendLogClick() {
        sendMail(this, list("dohan.rene@gmail.com"), mailSubject, textView(id.CSLog_LogText).title())
    }
}