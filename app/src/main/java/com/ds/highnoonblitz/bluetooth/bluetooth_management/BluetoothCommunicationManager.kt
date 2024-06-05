package com.ds.highnoonblitz.bluetooth.bluetooth_management

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.ds.highnoonblitz.bluetooth.communication_threads.BasicBluetoothCommunicationThread
import com.ds.highnoonblitz.messages.MessageComposed

open class BluetoothCommunicationManager(private val bluetoothManager: BluetoothManager) : BasicBluetoothCommunication {

    open val bluetoothAdapter: BluetoothAdapter = bluetoothManager.adapter
    open var bluetoothThread: BasicBluetoothCommunicationThread? = null

    @RequiresApi(Build.VERSION_CODES.S)
    override fun sendMessage(device: BluetoothDevice, message: MessageComposed) {
        bluetoothThread?.sendMessage(device, message)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun broadcastMessage(message: MessageComposed) {
        bluetoothThread?.sendBroadcastMessage(message)
    }


    override fun closeConnection() {
        TODO("Not yet implemented")
    }



}