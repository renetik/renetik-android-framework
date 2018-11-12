package renetik.android.view

import android.view.View
import renetik.android.java.callback.CSReturn
import renetik.android.lang.CSLang.MINUTE
import renetik.android.lang.CSLang.schedule
import renetik.android.rpc.CSResponse
import renetik.android.viewbase.CSViewController

/**
 * Created by renetik on 29/12/17.
 */

class CSAutoLoadController(parent: CSViewController<*>, runWith: CSReturn<CSResponse<*>>)
    : CSViewController<View>(parent) {

    private val autoReload = schedule(MINUTE, { runWith.invoke() })

    override fun onViewShowing() {
        super.onViewShowing()
        autoReload.start()
    }

    override fun onViewHiding() {
        super.onViewHiding()
        autoReload.stop()
    }

}
