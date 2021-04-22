package renetik.android.base

import androidx.annotation.LayoutRes

class CSLayoutRes(@LayoutRes val id: Int) {
    companion object {
        fun layout(@LayoutRes id: Int) = CSLayoutRes(id)
    }
}