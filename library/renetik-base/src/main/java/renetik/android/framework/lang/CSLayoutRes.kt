package renetik.android.framework.lang

import androidx.annotation.LayoutRes

class CSLayoutRes(@LayoutRes val id: Int) {
    companion object {
        fun layout(@LayoutRes id: Int) = CSLayoutRes(id)
    }
}