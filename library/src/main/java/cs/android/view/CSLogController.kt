package cs.android.view

import android.graphics.Color
import android.text.method.ScrollingMovementMethod
import android.view.View
import cs.android.R
import cs.android.R.id
import cs.android.R.layout
import cs.android.extensions.sendMail
import cs.android.extensions.view.*
import cs.android.viewbase.CSViewController
import cs.java.lang.CSLang.model

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

    private fun loadText() = logText.title(model().logger().logString())

    private fun onSendLogClick() {
        navigation.dialog("Send application log", "Enter target email")
                .showInput("Target email", model().store().loadString(sendLogMailKey, "")) { dialog ->
                    model().store().put(sendLogMailKey, dialog.inputValue())
                    sendMail(dialog.inputValue(), model().applicationName() +
                            " This is log from application sent as email attachment for application developer"
                            , logText.title())
                }
    }
}