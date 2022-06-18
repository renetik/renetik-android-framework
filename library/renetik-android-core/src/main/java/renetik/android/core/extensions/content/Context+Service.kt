package renetik.android.core.extensions.content

import android.app.Service
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import android.content.ContextWrapper.MIDI_SERVICE
import android.content.Intent
import android.media.midi.MidiManager
import android.view.inputmethod.InputMethodManager

val Context.bluetooth get() = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
val Context.input get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

val Context.isMidiSupported: Boolean
    get() = getSystemService(MIDI_SERVICE) != null

fun <T> Context.ifHasMidi(function: (MidiManager) -> T): T? = midi?.let { function(it) }

val Context.midi: MidiManager?
    get() = getSystemService(MIDI_SERVICE) as? MidiManager

fun Context.startService(serviceClass: Class<out Service>) =
    startService(Intent(this, serviceClass))

fun Context.stopService(serviceClass: Class<out Service>) =
    stopService(Intent(this, serviceClass))

