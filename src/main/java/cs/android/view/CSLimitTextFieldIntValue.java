package cs.android.view;

import cs.android.viewbase.CSViewController;

import static cs.java.lang.CSLang.*;

public class CSLimitTextFieldIntValue extends CSViewController {

    private int _minValue;
    private int _maxValue;
    private int _alertString;
    private int _beforeChangeValue;

    public CSLimitTextFieldIntValue(CSViewController parent, int id, int minValue, int maxValue, int alertString) {
        super(parent, id);
        _minValue = minValue;
        _maxValue = maxValue;
        _alertString = alertString;
        asView().setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                _beforeChangeValue = intValue();
            else if (intValue() > _maxValue || intValue() < _minValue) onWrongNumberEntered();
        });
    }

    private int intValue() {
        return asInt(text());
    }

    private void onWrongNumberEntered() {
        view().text(stringify(_beforeChangeValue));
        toast(getString(_alertString));
    }

    public void setMaxValue(int maxValue) {
        _maxValue = maxValue;
        validate();
    }

    private void validate() {
        if (intValue() > _maxValue) view().text(stringify(_maxValue));
        if (intValue() < _minValue) view().text(stringify(_minValue));
    }
}