package cs.android.view;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.widget.EditText;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import cs.android.CSContextInterface;
import cs.android.R;
import cs.android.viewbase.CSContextController;
import cs.java.callback.CSRun;
import cs.java.callback.CSRunWith;
import cs.java.callback.CSRunWithWith;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

import static cs.java.lang.CSLang.YES;
import static cs.java.lang.CSLang.is;
import static cs.java.lang.CSLang.no;
import static cs.java.lang.CSLang.run;
import static cs.java.lang.CSLang.set;

public class CSDialog extends CSContextController {

    private Builder _builder;
    private EditText _inputField;
    private AlertDialog _dialog;
    private boolean _cancelable = YES;
    private CSRunWith<Dialog> _onDismiss;

    public CSDialog(Context context) {
        super(context);
        _builder = new Builder(context);
    }

    public CSDialog(CSContextInterface hasContext) {
        this(hasContext.context());
    }

    public CSDialog(Context context, int theme) {
        super(context);
        _builder = new Builder(context, theme);
    }

    public CSDialog create(int message, final CSRun run) {
        return create(0, message, R.string.cs_dialog_ok, R.string.cs_dialog_cancel, value -> {
            if (value == R.string.cs_dialog_ok) run.run();
            hideKeyboard();
        });
    }

    public CSDialog show(int message, final CSRun run) {
        return create(message, run).show();
    }

    public CSDialog create(int title, int message, int ok, int cancel, CSRunWith<Integer> call) {
        return create(getString(title), getString(message), ok, cancel, call);
    }

    public CSDialog create(String title, String message, int dialogOk, int dialogCancel, CSRunWith<Integer> call) {
        text(title, message);
        buttons(dialogOk, dialogCancel, call);
        return this;
    }

    public void create(String title, int arrayId, String dialogCancel, final CSRunWith<Integer> runWith) {
        text(title, "");
        _builder.setNegativeButton(dialogCancel, null);
        _builder.setItems(arrayId, (dialog, which) -> runWith.run(which));
    }

    public CSDialog create(String title, List<String> items, String dialogCancel, final CSRunWith<Integer> runWith) {
        text(title, "");
        _builder.setNegativeButton(dialogCancel, null);
        _builder.setItems(items.toArray(new CharSequence[items.size()]), (dialog, which) -> runWith.run(which));
        return this;
    }

    public CSDialog text(String title, String message) {
        if (set(title)) _builder.setTitle(title);
        if (set(message)) _builder.setMessage(message);
        return this;
    }

    public CSDialog buttons(final int dialogOk, final CSRunWith<Integer> call) {
        _builder.setPositiveButton(dialogOk, (dialog, which) -> {
            if (is(call)) call.run(dialogOk);
            hideKeyboard();
        });
        return this;
    }

    public void buttons(final int dialogOk, final int dialogCancel, final CSRunWith<Integer> call) {
        _builder.setPositiveButton(dialogOk, (dialog, which) -> {
            run(call, dialogOk);
            hideKeyboard();
        });
        if (set(dialogCancel)) _builder.setNegativeButton(dialogCancel, (dialog, which) -> {
            run(call, dialogCancel);
            hideKeyboard();
        });
    }

    public CSDialog create(String title, String message, String dialogOk, String dialogCancel, CSRunWithWith<String, CSDialog> call) {
        text(title, message);
        buttons(dialogOk, dialogCancel, call);
        return this;
    }

    public CSDialog buttons(final String dialogOk, final CSRunWith<String> call) {
        _builder.setPositiveButton(dialogOk, (dialog, which) -> {
            if (is(call)) call.run(dialogOk);
            hideKeyboard();
        });
        return this;
    }

    public CSDialog buttons(final String dialogOk, final String dialogCancel, final CSRunWithWith<String, CSDialog> call) {
        _builder.setPositiveButton(dialogOk, (dialog, which) -> {
            if (is(call)) call.run(dialogOk, CSDialog.this);
            hideKeyboard();
        });
        if (set(dialogCancel)) _builder.setNegativeButton(dialogCancel, (dialog, which) -> {
            if (is(call)) call.run(dialogCancel, CSDialog.this);
            hideKeyboard();
        });
        return this;
    }

    public void hideKeyboard() {
        if (is(_dialog.getCurrentFocus()))
            hideKeyboard(_dialog.getCurrentFocus().getWindowToken());
    }

    public CSDialog create(String title, String message, String dialogOk, String dialogCancel, String neutral, CSRunWithWith<String, CSDialog> call) {
        text(title, message);
        buttons(dialogOk, dialogCancel, neutral, call);
        return this;
    }

    public CSDialog buttons(final String dialogOk, final String dialogCancel, final String neutral, final CSRunWithWith<String, CSDialog> call) {
        buttons(dialogOk, dialogCancel, call);
        _builder.setNeutralButton(neutral, (dialog, which) -> {
            if (is(call)) call.run(neutral, CSDialog.this);
            hideKeyboard();
        });
        return this;
    }

    public CSDialog icon(int icon) {
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

    public CSDialog text(String string) {
        return text(string, "");
    }

    public CSDialog show(int title, int message, int ok, int cancel, CSRunWith<Integer> call) {
        return show(getString(title), getString(message), ok, cancel, call);
    }

    public CSDialog show(String title, String message, int dialogOk, int dialogCancel, CSRunWith<Integer> call) {
        text(title, message);
        buttons(dialogOk, dialogCancel, call);
        show();
        return this;
    }

    public CSDialog show(String title, String message, int dialogOk, int dialogCancel, CSRun onOkClick) {
        text(title, message);
        buttons(dialogOk, dialogCancel, value -> {if (dialogOk == value) onOkClick.run();});
        show();
        return this;
    }

    public CSDialog show() {
        _dialog = _builder.show();
        _dialog.setOnDismissListener(dialog -> {
            if (is(_onDismiss)) _onDismiss.run(_dialog);
        });
        _dialog.setCanceledOnTouchOutside(_cancelable);
        _dialog.setCancelable(_cancelable);
        return this;
    }

    public CSDialog hide() {
        if (is(_dialog)) _dialog.dismiss();
        return this;
    }


    public CSDialog show(String title, String message, String dialogOK, CSRunWithWith<String, CSDialog> call) {
        text(title, message);
        buttons(dialogOK, null, call);
        show();
        return this;
    }

    public CSDialog show(String title, String message, String dialogOk, String dialogCancel, CSRunWithWith<String, CSDialog> call) {
        text(title, message);
        buttons(dialogOk, dialogCancel, call);
        show();
        return this;
    }

    public CSDialog inputType(int type) {
        inputField().setInputType(type);
        return this;
    }

    public CSDialog cancelable(boolean cancelable) {
        _cancelable = cancelable;
        return this;
    }

    public CSDialog onDismiss(CSRunWith<Dialog> onDismiss) {
        _onDismiss = onDismiss;
        return this;
    }


    public CSDialog inputFieldText(String textValue) {
        inputField().setText(textValue);
        return this;
    }

    public void show(@Nullable String title, @Nullable String message, @Nullable Function0<Unit> function) {
        show(title, message, R.string.cs_dialog_ok, R.string.cs_dialog_cancel, value -> {
            if (is(function)) function.invoke();
        });
    }

    public void show(@Nullable String title, @Nullable Function0<Unit> function) {
        show(title, null, R.string.cs_dialog_ok, R.string.cs_dialog_cancel,value -> {
            if (is(function)) function.invoke();
        });
    }

    public void show(@Nullable String title) {
        show(title, null);
    }

    public void showMessage(@Nullable String message) {
        show(null, message, R.string.cs_dialog_ok, 0, (CSRun) null);
    }
}
