package renetik.android.ui.view.adapter

import android.view.View
import java.lang.System.currentTimeMillis

class CSClickAdapter(private val onClickListener: View.OnClickListener) : View.OnClickListener {
    private var lastTime: Long = 0

    override fun onClick(v: View?) {
        val current = currentTimeMillis()
        if ((current - lastTime) > 400) {
            onClickListener.onClick(v)
            lastTime = current
        }
    }
}