package com.ds.highnoonblitz.view.components

import PermissionManager
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.ds.highnoonblitz.bluetooth.BluetoothHandler
import com.ds.highnoonblitz.messages.MessageFactory

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ReadyButton(activity: Activity, bluetoothHandler: BluetoothHandler) {
    val isReady = remember { mutableStateOf(false) }

    Button(onClick = {
        isReady.value = !isReady.value
        var deviceName = "Unknown device"
        try{
            deviceName = bluetoothHandler.getBluetoothAdapter()?.name ?: "Unknown device"
        } catch (e: SecurityException) {
            bluetoothHandler.getBluetoothAdapter()?.let {
                PermissionManager.getBluetoothPermission(activity, it)
            }
        }
        val msg = MessageFactory.createReadyMessage(deviceName, isReady.value)
        bluetoothHandler.broadcastMessage(msg)

    }) {
        Text(text = if (isReady.value) "Not Ready" else "Ready")
    }
}