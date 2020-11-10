package renetik.android.view.adapter

import android.view.View

class CSClickAdapter(private val onClickListener: View.OnClickListener) : View.OnClickListener {
    private var lastTime: Long = 0

    override fun onClick(v: View?) {
        val current = System.currentTimeMillis()
        if ((current - lastTime) > 500) {
            onClickListener.onClick(v)
            lastTime = current
        }
    }
}