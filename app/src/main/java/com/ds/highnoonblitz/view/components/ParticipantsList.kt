package com.ds.highnoonblitz.view.components

import PermissionManager
import android.app.Activity
import android.bluetooth.BluetoothSocket
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ds.highnoonblitz.bluetooth.BluetoothHandler
import com.ds.highnoonblitz.bluetooth.communication_threads.CommunicationChannelThread
import com.ds.highnoonblitz.messages.MessageFactory

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ParticipantsList(activity: Activity, bluetoothHandler: BluetoothHandler) {

    @Composable
    fun getButtonColor(bluetoothHandler: BluetoothHandler, device: Pair<BluetoothSocket, CommunicationChannelThread>, deviceName: String): Color {
        return when {
            bluetoothHandler.getDeviceManager().getMasterAddress().value == device.first.remoteDevice.address -> Color(33, 150, 243, 255)
            bluetoothHandler.getDeviceManager().getAllReadyDevices().contains(deviceName) -> Color(0, 200, 70)
            else -> MaterialTheme.colorScheme.secondary
        }
    }

    Text(text = "Connected devices",
        modifier = Modifier
            .padding(top = 16.dp, bottom = 8.dp),
        style = MaterialTheme.typography.headlineMedium)

    LazyColumn(modifier = Modifier.height(250.dp)
    ) {
        itemsIndexed(bluetoothHandler.getDeviceManager().getAllConnectedSockets()) { index, device ->
            ObserveReadyDevices(bluetoothHandler = bluetoothHandler)
            var deviceName = "Unknown device"
            try{
                deviceName = device.first.remoteDevice.name
            } catch (e: SecurityException) {
                bluetoothHandler.getBluetoothAdapter()?.let {
                    PermissionManager.getBluetoothPermission(activity, it)
                }
            }
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
                val buttonColor = getButtonColor(bluetoothHandler, device, deviceName)
                DiscoveredDeviceButton(buttonText = deviceName, buttonAction = {
                    bluetoothHandler.sendMessage(MessageFactory.createTestMessage(), device.first.remoteDevice)
                }, buttonColor = buttonColor)
            }
        }
    }


}