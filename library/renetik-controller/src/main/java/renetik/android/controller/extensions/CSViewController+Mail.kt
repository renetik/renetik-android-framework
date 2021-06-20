package renetik.android.controller.extensions

import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.os.Environment.getExternalStorageDirectory
import renetik.android.controller.base.CSActivityView
import renetik.android.java.extensions.collections.list
import java.io.File

fun <T : CSActivityView<*>> T.sendMail(email: String, subject: String, text: String) {
    sendMail(list(email), subject, text, list())
}

fun <T : CSActivityView<*>> T.sendMail(emails: List<String>, subject: String, body: CharSequence,
                                       attachment: File) {
    sendMail(emails, subject, body, list(attachment))
}

fun <T : CSActivityView<*>> T.sendMail(emails: List<String>, subject: String, body: CharSequence,
                                       attachments: List<File>) {
    Intent(if (attachments.isEmpty()) ACTION_SEND else ACTION_SEND_MULTIPLE).apply {
        putExtra(EXTRA_EMAIL, emails.toTypedArray()).putExtra(EXTRA_SUBJECT, subject)
        putExtra(EXTRA_TEXT, body).type = "text/plain"
        val attachmentUris = ArrayList<Uri>()
        attachments.forEach { file ->
            if (!file.startsWith(getExternalStorageDirectory()))
                throw Exception("Attachment not in ExternalStorageDirectory")
            else if (!(file.exists() && file.canRead())) throw Exception("Attachment can not be read")
            attachmentUris.add(Uri.fromFile(file))
        }
        if (attachments.isNotEmpty()) putParcelableArrayListExtra(EXTRA_STREAM, attachmentUris)
        startActivity(createChooser(this, "Pick an Email provider"))
    }
}