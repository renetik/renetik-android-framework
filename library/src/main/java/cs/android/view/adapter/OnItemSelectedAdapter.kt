package cs.android.view.adapter

import android.view.View
import android.widget.AdapterView

open class OnItemSelectedAdapter : AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        onItemSelected(position)
    }

   open fun onItemSelected(position: Int) {

    }

}