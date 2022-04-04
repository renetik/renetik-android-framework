package renetik.android.content

import android.app.Service
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import android.content.ContextWrapper.MIDI_SERVICE
import android.content.Intent
import android.media.midi.MidiManager
import android.os.Build
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi

val Context.bluetooth get() = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
val Context.input get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

val Context.isMidiSupported: Boolean
    @RequiresApi(Build.VERSION_CODES.M) get() = getSystemService(MIDI_SERVICE) != null

@RequiresApi(Build.VERSION_CODES.M)
fun <T> Context.ifMidiSupported(function: () -> T): T? = if (isMidiSupported) function() else null

val Context.midi: MidiManager
    @RequiresApi(Build.VERSION_CODES.M) get() = getSystemService(MIDI_SERVICE) as MidiManager

fun Context.startService(serviceClass: Class<out Service>) =
    startService(Intent(this, serviceClass))

fun Context.stopService(serviceClass: Class<out Service>) =
    stopService(Intent(this, serviceClass))

