package renetik.android.core.android.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context

val Context.bluetooth get() = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
val Context.bluetoothAdapter: BluetoothAdapter? get() = bluetooth.adapter
val BluetoothManager.isEnabled: Boolean get() = adapter?.isEnabled == true