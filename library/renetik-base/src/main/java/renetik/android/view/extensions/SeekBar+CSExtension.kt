package renetik.android.view.extensions

import android.widget.SeekBar

fun SeekBar.onProgressChanged(function: (progress: Int) -> Unit) {
    setOnSeekBarChangeListener(object :
        SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            function(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }
    })
}