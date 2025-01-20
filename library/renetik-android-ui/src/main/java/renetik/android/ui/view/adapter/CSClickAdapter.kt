package renetik.android.ui.view.adapter

import android.view.View
import java.lang.System.currentTimeMillis

class CSClickAdapter(
    timeout: Int? = null,
    private val onClick: () -> Unit
) : View.OnClickListener {
    private var lastTime: Long = 0
    private val timeout = timeout ?: 250

    override fun onClick(view: View) {
        val current = currentTimeMillis()
        if ((current - lastTime) > timeout) {
            lastTime = current
            onClick()
        }
    }
}