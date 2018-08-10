package cs.android.view

import android.widget.EditText
import cs.android.extensions.view.title
import cs.android.viewbase.CSViewController
import cs.java.lang.CSLang.*

class CSLimitTextFieldIntValue(parent: CSViewController<*>, id: Int, val minValue: Int,
                               var maxValue: Int, val alertString: Int) : CSViewController<EditText>(parent, id) {
    private var beforeChangeValue: Int = 0

    init {
        view?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) beforeChangeValue = intValue()
            else if (intValue() > maxValue || intValue() < minValue) onWrongNumberEntered()
        }
    }

    private fun intValue(): Int {
        return asInt(view.title())
    }

    private fun onWrongNumberEntered() {
        view.title(stringify(beforeChangeValue))
        toast(getString(alertString))
    }

    fun maxValue(maxValue: Int) {
        this.maxValue = maxValue
        validate()
    }

    private fun validate() {
        if (intValue() > maxValue) view.title(stringify(maxValue))
        if (intValue() < minValue) view.title(stringify(minValue))
    }
}