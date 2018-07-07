package cs.android.util;

import android.content.Intent;
import android.net.Uri;

import cs.android.viewbase.CSViewController;
import cs.java.collections.CSList;

import static cs.java.lang.CSLang.is;
import static cs.java.lang.CSLang.list;
import static cs.java.lang.CSLang.warn;

public class CSMail {

    public static void sendMail(CSViewController controller, String email, String subject, String text) {
        sendMail(controller, list(email), subject, text, null);
    }

    public static void sendMail(CSViewController controller, CSList<String> emails, String subject,
                                String text) {
        sendMail(controller, emails, subject, text, null);
    }

    public static void sendMail(CSViewController controller, CSList<String> emails, String subject,
                                String text, String attachmentFilePath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, emails.toArray(new String[0]));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        if (is(attachmentFilePath))
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + attachmentFilePath));
        try {
            controller.startActivity(Intent.createChooser(intent, subject));
        } catch (Exception ex) {
            warn("Send mail failed", ex);
        }
    }
}
