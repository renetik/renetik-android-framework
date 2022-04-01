package renetik.android.content

import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import android.content.ContextWrapper.MIDI_SERVICE
import android.media.midi.MidiManager
import android.os.Build
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi

val Context.bluetooth: BluetoothManager get() = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
val Context.input: InputMethodManager get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

val Context.isMidiSupported: Boolean
    @RequiresApi(Build.VERSION_CODES.M) get() = getSystemService(MIDI_SERVICE) != null

@RequiresApi(Build.VERSION_CODES.M)
fun <T> Context.isMidiSupported(function: () -> T): T? = if (isMidiSupported) function() else null

val Context.midi: MidiManager
    @RequiresApi(Build.VERSION_CODES.M) get() = getSystemService(MIDI_SERVICE) as MidiManager

