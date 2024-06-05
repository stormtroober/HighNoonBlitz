package com.ds.highnoonblitz.view.components

import androidx.compose.runtime.Composable
import com.ds.highnoonblitz.bluetooth.BluetoothHandler

@Composable
fun ObserveReadyDevices(bluetoothHandler: BluetoothHandler) {
    val readyDevices = bluetoothHandler.getDeviceManager().getAllReadyDevices()
}