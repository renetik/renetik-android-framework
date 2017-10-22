package cs.android.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.widget.EditText;

import java.util.List;

import cs.android.CSActivityInterface;
import cs.android.R;
import cs.android.viewbase.CSContextController;
import cs.java.callback.CSRun;
import cs.java.callback.CSRunWith;
import cs.java.callback.CSRunWithWith;

import static cs.java.lang.CSLang.YES;
import static cs.java.lang.CSLang.is;
import static cs.java.lang.CSLang.no;
import static cs.java.lang.CSLang.set;

public class CSAlertDialog extends CSContextController {

    private Builder _builder;
    private EditText _inputField;
    private AlertDialog _dialog;
    private boolean _cancelable = YES;
    private CSRunWith<Dialog> _onDismiss;

    public CSAlertDialog(Context context) {
        super(context);
        _builder = new Builder(context);
    }

    public CSAlertDialog(CSActivityInterface hasActivity) {
        this(hasActivity.context());
    }

    public CSAlertDialog(Context context, int theme) {
        super(context);
        _builder = new Builder(context, theme);
    }

    public CSAlertDialog create(int message, final CSRun run) {
        return create(0, message, R.string.dialog_ok, R.string.dialog_cancel, new CSRunWith<Integer>() {
            public void run(Integer value) {
                if (value == R.string.dialog_ok) run.run();
                hideKeyboard();
            }
        });
    }

    public CSAlertDialog show(int message, final CSRun run) {
        return create(message, run).show();
    }

    public CSAlertDialog create(int title, int message, int ok, int cancel, CSRunWith<Integer> call) {
        return create(getString(title), getString(message), ok, cancel, call);
    }

    public CSAlertDialog create(String title, String message, int dialogOk, int dialogCancel, CSRunWith<Integer> call) {
        text(title, message);
        buttons(dialogOk, dialogCancel, call);
        return this;
    }

    public void create(String title, int arrayId, String dialogCancel, final CSRunWith<Integer> runWith) {
        text(title, "");
        _builder.setNegativeButton(dialogCancel, null);
        _builder.setItems(arrayId, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                runWith.run(which);
            }
        });
    }

    public CSAlertDialog create(String title, List<String> items, String dialogCancel, final CSRunWith<Integer> runWith) {
        text(title, "");
        _builder.setNegativeButton(dialogCancel, null);
        _builder.setItems(items.toArray(new CharSequence[items.size()]), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                runWith.run(which);
            }
        });
        return this;
    }

    public CSAlertDialog text(String title, String message) {
        if (set(title)) _builder.setTitle(title);
        if (set(message)) _builder.setMessage(message);
        return this;
    }

    public CSAlertDialog buttons(final int dialogOk, final CSRunWith<Integer> call) {
        _builder.setPositiveButton(dialogOk, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (is(call)) call.run(dialogOk);
                hideKeyboard();
            }
        });
        return this;
    }

    public void buttons(final int dialogOk, final int dialogCancel, final CSRunWith<Integer> call) {
        _builder.setPositiveButton(dialogOk, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (is(call)) call.run(dialogOk);
                hideKeyboard();
            }
        });
        if (set(dialogCancel)) _builder.setNegativeButton(dialogCancel, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (is(call)) call.run(dialogCancel);
                hideKeyboard();
            }
        });
    }

    public CSAlertDialog create(String title, String message, String dialogOk, String dialogCancel, CSRunWithWith<String, CSAlertDialog> call) {
        text(title, message);
        buttons(dialogOk, dialogCancel, call);
        return this;
    }

    public CSAlertDialog buttons(final String dialogOk, final CSRunWith<String> call) {
        _builder.setPositiveButton(dialogOk, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (is(call)) call.run(dialogOk);
                hideKeyboard();
            }
        });
        return this;
    }

    public CSAlertDialog buttons(final String dialogOk, final String dialogCancel, final CSRunWithWith<String, CSAlertDialog> call) {
        _builder.setPositiveButton(dialogOk, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (is(call)) call.run(dialogOk, CSAlertDialog.this);
                hideKeyboard();
            }
        });
        if (set(dialogCancel)) _builder.setNegativeButton(dialogCancel, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (is(call)) call.run(dialogCancel, CSAlertDialog.this);
                hideKeyboard();
            }
        });
        return this;
    }

    public void hideKeyboard() {
        if (is(_dialog.getCurrentFocus()))
            hideKeyboard(_dialog.getCurrentFocus().getWindowToken());
    }

    public CSAlertDialog create(String title, String message, String dialogOk, String dialogCancel, String neutral, CSRunWithWith<String, CSAlertDialog> call) {
        text(title, message);
        buttons(dialogOk, dialogCancel, neutral, call);
        return this;
    }

    public CSAlertDialog buttons(final String dialogOk, final String dialogCancel, final String neutral, final CSRunWithWith<String, CSAlertDialog> call) {
        buttons(dialogOk, dialogCancel, call);
        _builder.setNeutralButton(neutral, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (is(call)) call.run(neutral, CSAlertDialog.this);
                hideKeyboard();
            }
        });
        return this;
    }

    public CSAlertDialog icon(int icon) {
        _builder.setIcon(icon);
        return this;
    }

    public String inputFieldText() {
        return inputField().getText().toString();
    }

    public EditText inputField() {
        if (no(_inputField)) _inputField = new EditText(context());
        _builder.setView(_inputField);
        return _inputField;
    }

    public void text(final int title, final int message) {
        if (set(title)) _builder.setTitle(title);
        if (set(message)) _builder.setMessage(message);
    }

    public CSAlertDialog text(String string) {
        return text(string, "");
    }

    public CSAlertDialog show(int title, int message, int ok, int cancel, CSRunWith<Integer> call) {
        return show(getString(title), getString(message), ok, cancel, call);
    }

    public CSAlertDialog show(String title, String message, int dialogOk, int dialogCancel, CSRunWith<Integer> call) {
        text(title, message);
        buttons(dialogOk, dialogCancel, call);
        show();
        return this;
    }

    public CSAlertDialog show() {
        _dialog = _builder.show();
        _dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                if (is(_onDismiss)) _onDismiss.run(_dialog);
            }
        });
        _dialog.setCanceledOnTouchOutside(_cancelable);
        _dialog.setCancelable(_cancelable);
        return this;
    }

    public CSAlertDialog hide() {
        if (is(_dialog)) _dialog.dismiss();
        return this;
    }


    public CSAlertDialog show(String title, String message, String dialogOK, CSRunWithWith<String, CSAlertDialog> call) {
        text(title, message);
        buttons(dialogOK, null, call);
        show();
        return this;
    }

    public CSAlertDialog show(String title, String message, String dialogOk, String dialogCancel, CSRunWithWith<String, CSAlertDialog> call) {
        text(title, message);
        buttons(dialogOk, dialogCancel, call);
        show();
        return this;
    }

    public CSAlertDialog inputType(int type) {
        inputField().setInputType(type);
        return this;
    }

    public CSAlertDialog cancelable(boolean cancelable) {
        _cancelable = cancelable;
        return this;
    }

    public CSAlertDialog onDismiss(CSRunWith<Dialog> onDismiss) {
        _onDismiss = onDismiss;
        return this;
    }


    public CSAlertDialog inputFieldText(String textValue) {
        inputField().setText(textValue);
        return this;
    }
}
