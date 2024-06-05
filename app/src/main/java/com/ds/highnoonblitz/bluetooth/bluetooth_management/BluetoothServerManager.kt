package com.ds.highnoonblitz.bluetooth.bluetooth_management

import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.os.Build
import androidx.annotation.RequiresApi
import com.ds.highnoonblitz.MainActivity
import com.ds.highnoonblitz.bluetooth.communication_threads.ServerThread

class BluetoothServerManager(
    private val activity: MainActivity,
    private val bluetoothManager: BluetoothManager,
    private val deviceManager: BluetoothDeviceManager,
    private val onDeviceDisconnected: (BluetoothSocket) -> Unit,

) : BluetoothCommunicationManager(bluetoothManager) {


    @RequiresApi(Build.VERSION_CODES.S)
    fun startBluetoothServer() {
        if(bluetoothAdapter.isEnabled){
            bluetoothThread = ServerThread(activity, bluetoothAdapter, deviceManager, onDeviceDisconnected)
            bluetoothThread?.start()
        }
    }



}