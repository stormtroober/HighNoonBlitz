package com.ds.highnoonblitz.bluetooth.bluetooth_management

import PermissionManager
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.ds.highnoonblitz.MainActivity
import com.ds.highnoonblitz.bluetooth.communication_threads.ClientThread

class BluetoothClientManager(private val activity: MainActivity, private val bluetoothManager: BluetoothManager,
                             private val deviceManager: BluetoothDeviceManager,
                             private val onDeviceDisconnected: (BluetoothSocket) -> Unit) : BluetoothCommunicationManager(
    bluetoothManager
) {
    private var receiver: BroadcastReceiver? = null

    @RequiresApi(Build.VERSION_CODES.S)
    fun connectToDevice(deviceMac: String) {
        val device = bluetoothAdapter.getRemoteDevice(deviceMac)
        if(device == null){
            Log.i("BluetoothClientManager", "Device is null")
        }
        connect(device)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun connect(device: BluetoothDevice) {
        if (bluetoothAdapter.isEnabled && !deviceManager.isDeviceAlreadyConnected(device)) {
            bluetoothThread = ClientThread(activity, bluetoothAdapter, device, deviceManager, onDeviceDisconnected)
            bluetoothThread!!.start()
        } else {
            Log.i("BluetoothClientManager", "Device is already connected")
        }
    }


    @RequiresApi(Build.VERSION_CODES.S)
    fun discoveryDevices(discoveredDevices: MutableList<BluetoothDevice>){
        try {
            discoveredDevices.clear()
            bluetoothAdapter.startDiscovery()
            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    Log.i("BluetoothClientManager", "device found")
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (device != null && !discoveredDevices.contains(device)) {
                        discoveredDevices.add(device)
                    }
                }
            }

            activity.registerReceiver(receiver, filter)
        } catch (e: SecurityException) {
            PermissionManager.getBluetoothPermission(activity, bluetoothAdapter)
        }


    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun disconnectFromLobby() {
        Log.i("BluetoothClientManager", "Starting disconnecting from lobby procedure")

    }


}