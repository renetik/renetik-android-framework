package renetik.android.view

import android.widget.EditText
import renetik.android.extensions.string
import renetik.android.extensions.view.title
import renetik.android.viewbase.CSViewController
import renetik.android.lang.CSLang.asInt
import renetik.android.lang.CSLang.toast

class CSLimitTextFieldIntValue(parent: CSViewController<*>, id: Int, val minValue: Int,
                               private var maxValue: Int, private val alertString: Int)
    : CSViewController<EditText>(parent, id) {

    private var beforeChangeValue: Int = 0

    init {
        view?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) beforeChangeValue = intValue()
            else if (intValue() > maxValue || intValue() < minValue) onWrongNumberEntered()
        }
    }

    private fun intValue(): Int {
        return asInt(view.title())
    }

    private fun onWrongNumberEntered() {
        view.title(string(beforeChangeValue))
        toast(stringRes(alertString))
    }

    fun maxValue(maxValue: Int) {
        this.maxValue = maxValue
        validate()
    }

    private fun validate() {
        if (intValue() > maxValue) view.title(string(maxValue))
        if (intValue() < minValue) view.title(string(minValue))
    }
}