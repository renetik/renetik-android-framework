package cs.android.view

import android.text.method.ScrollingMovementMethod
import android.view.View
import cs.android.R.id
import cs.android.R.layout
import cs.android.extensions.sendMail
import cs.android.extensions.view.onClick
import cs.android.extensions.view.textView
import cs.android.extensions.view.title
import cs.android.extensions.view.view
import cs.android.viewbase.CSViewController
import cs.java.lang.CSLang.model

class CSLogController(parent: CSViewController<*>) :
        CSViewController<View>(parent, layout(layout.cs_log)), CSNavigationItem {

    private val logText = textView(id.CSLog_LogText).apply { movementMethod = ScrollingMovementMethod() }

    init {
        menu("Send to developer").onClick { onSendLogClick() }
    }

    override fun onCreate() {
        super.onCreate()
        loadText()
        view(id.CSLog_Reload).onClick { loadText() }
    }

    private fun loadText() = logText.title(model().logger().logString())

    private fun onSendLogClick() = sendMail("dohan.rene@gmail.com", model().applicationName() +
            " This is log from application sent as email attachment for application developer"
            , logText.title())
}