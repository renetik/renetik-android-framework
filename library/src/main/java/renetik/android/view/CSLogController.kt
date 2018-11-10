package renetik.android.view

import android.graphics.Color
import android.text.method.ScrollingMovementMethod
import android.view.View
import renetik.android.R
import renetik.android.R.id
import renetik.android.R.layout
import renetik.android.application
import renetik.android.extensions.sendMail
import renetik.android.extensions.view.*
import renetik.android.viewbase.CSViewController

const val sendLogMailKey = "send_log_mail"

class CSLogController(val navigation: CSNavigationController) :
        CSViewController<View>(navigation, layout(layout.cs_log)), CSNavigationItem {

    private val logText = textView(id.CSLog_LogText).apply { movementMethod = ScrollingMovementMethod() }

    init {
        menu("Send to developer").onClick { onSendLogClick() }
    }

    override fun onCreate() {
        super.onCreate()
        floatingButton(R.id.CSLog_Reload).onClick { loadText() }.iconTint(Color.WHITE)
        loadText()
    }

    private fun loadText() = logText.title(application.logger.logString())

    private fun onSendLogClick() {
        navigation.dialog("Send application log", "Enter target email")
                .showInput("Target email", application.store.loadString(sendLogMailKey, "")) { dialog ->
                    application.store.put(sendLogMailKey, dialog.inputValue())
                    sendMail(dialog.inputValue(), application.name +
                            " This is log from application sent as email attachment for application developer"
                            , logText.title())
                }
    }
}