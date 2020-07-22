package renetik.android.view.extensions

import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener

fun SeekBar.onChange(function: (value: Int) -> Unit) = apply {
    setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) =
            function(progress)

        override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

        override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
    })
}

fun SeekBar.value(value: Int) = apply { progress = value }