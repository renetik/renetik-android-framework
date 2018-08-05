package cs.android.extensions

import cs.android.CSContextInterface
import cs.android.view.CSDialog

fun CSContextInterface.dialog(title: String, onAccepted: () -> Unit) {
    CSDialog(context()).show(title) { onAccepted() }
}

fun CSContextInterface.dialog(): CSDialog {
    return CSDialog(context())
}