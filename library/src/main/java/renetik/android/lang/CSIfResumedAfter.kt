package renetik.android.lang

import renetik.android.lang.CSLang.doLater
import renetik.android.viewbase.CSViewController

class CSIfResumedAfter(parent: CSViewController<*>, milliseconds: Int, private val run: () -> Unit) {

    init {
        doLater(milliseconds, {
            if (parent.isResumed) run()
            onFinally()
        })
    }

    protected fun onFinally() {}
}
