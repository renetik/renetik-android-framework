package renetik.android.controller.common

import android.text.method.ScrollingMovementMethod
import android.view.View
import renetik.android.base.application
import renetik.android.base.layout
import renetik.android.controller.R
import renetik.android.controller.base.CSViewController
import renetik.android.controller.extensions.sendMail
import renetik.android.dialog.extensions.dialog
import renetik.android.extensions.textView
import renetik.android.material.extensions.floatingButton
import renetik.android.view.extensions.onClick
import renetik.android.view.extensions.title

const val sendLogMailKey = "send_log_mail"

class CSLogDisplayController(val navigation: CSNavigationController, val title: String = "Application Log") :
        CSViewController<View>(navigation, layout(R.layout.cs_log_panel)), CSNavigationItem {

    private val logText = textView(R.id.CSLog_LogText).apply { movementMethod = ScrollingMovementMethod() }

    init {
        menuItem("Send to developer").onClick { onSendLogClick() }
    }

    override fun onCreate() {
        super.onCreate()
        textView(R.id.CSLog_Title).title(title)
        floatingButton(R.id.CSLog_Reload).onClick { loadText() }
        loadText()
    }

    private fun loadText() = logText.title(application.logger.logString())

    private fun onSendLogClick() {
        dialog("Send application log", "Enter target email")
                .showInput("Target email", application.store.loadString(sendLogMailKey, "")) { dialog ->
                    application.store.put(sendLogMailKey, dialog.inputValue())
                    sendMail(dialog.inputValue(), application.name +
                            " This is log from application sent as email attachment for application developer"
                            , logText.title())
                }
    }
}