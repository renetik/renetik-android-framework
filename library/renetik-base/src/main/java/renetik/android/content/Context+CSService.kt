package renetik.android.content

import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import android.content.ContextWrapper.MIDI_SERVICE
import android.media.midi.MidiManager
import android.os.Build
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi

val Context.bluetooth: BluetoothManager get() = service(BLUETOOTH_SERVICE)
val Context.input: InputMethodManager get() = service(Context.INPUT_METHOD_SERVICE)
val Context.midi: MidiManager
    @RequiresApi(Build.VERSION_CODES.M) get() = service(MIDI_SERVICE)

