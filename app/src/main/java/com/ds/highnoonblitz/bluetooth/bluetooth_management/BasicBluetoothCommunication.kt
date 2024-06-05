package com.ds.highnoonblitz.bluetooth.bluetooth_management

import android.bluetooth.BluetoothDevice
import com.ds.highnoonblitz.messages.MessageComposed

interface BasicBluetoothCommunication {
    fun sendMessage(device: BluetoothDevice, message: MessageComposed)
    fun broadcastMessage(message: MessageComposed)
    fun closeConnection()

}