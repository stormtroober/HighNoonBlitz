package com.ds.highnoonblitz.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.ds.highnoonblitz.MainActivity
import com.ds.highnoonblitz.bluetooth.bluetooth_management.BluetoothClientManager
import com.ds.highnoonblitz.bluetooth.bluetooth_management.BluetoothDeviceManager
import com.ds.highnoonblitz.bluetooth.bluetooth_management.BluetoothServerManager
import com.ds.highnoonblitz.leader_election_algorithm.BullyElectionManager
import com.ds.highnoonblitz.messages.MessageComposed
import com.ds.highnoonblitz.messages.MessageFactory


class BluetoothHandler(private val activity: MainActivity){
    @RequiresApi(Build.VERSION_CODES.S)
    private val electionManager = BullyElectionManager(::onElectionTimerFinished)
    @RequiresApi(Build.VERSION_CODES.S)
    private val deviceManager: BluetoothDeviceManager = BluetoothDeviceManager(electionManager)
    private val bluetoothManager: BluetoothManager = activity.getSystemService(BluetoothManager::class.java)
    private val bluetoothAdapter = bluetoothManager.adapter
    private var bluetoothClientManager: BluetoothClientManager? = null
    private var bluetoothServerManager: BluetoothServerManager? = null

    @RequiresApi(Build.VERSION_CODES.S)
    @Synchronized
    fun onDeviceDisconnected(socket: BluetoothSocket) {
        deviceManager.removeSocket(socket)
        if(socket.remoteDevice.address == deviceManager.getMasterAddress().value){
            val electionMessage = MessageFactory.createElectionMessage()
            deviceManager.startElection()
            for(deviceMac in deviceManager.getElectionList()){
                val device = bluetoothAdapter.getRemoteDevice(deviceMac)
                sendMessage(electionMessage, device)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun onElectionTimerFinished() {
        broadcastMessage(MessageFactory.createConfigurationMessage(deviceManager.getMacAddresses()))
        deviceManager.setMaster(true)
        deviceManager.setMasterAddress("")
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    fun startBluetoothServer() {
        bluetoothServerManager = BluetoothServerManager(activity, bluetoothManager, deviceManager, ::onDeviceDisconnected)
        bluetoothServerManager!!.startBluetoothServer()
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    fun startBluetoothClientAndDiscovery(discoveredDevices: MutableList<BluetoothDevice>) {
        bluetoothClientManager = BluetoothClientManager(activity, bluetoothManager, deviceManager, ::onDeviceDisconnected)
        bluetoothClientManager?.let {
            bluetoothClientManager!!.discoveryDevices(discoveredDevices)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun connectToDevice(deviceMac: String) {
        bluetoothClientManager?.connectToDevice(deviceMac)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun sendMessage(message : MessageComposed, device: BluetoothDevice) {
        bluetoothClientManager?.sendMessage(device, message)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun broadcastMessage(message: MessageComposed) {
        Log.i("bluetoothHandler", "broadcastMessage")
        bluetoothClientManager?.broadcastMessage(message)
    }

    fun getBluetoothAdapter(): BluetoothAdapter? {
        return bluetoothAdapter
    }
    @RequiresApi(Build.VERSION_CODES.S)
    fun getDeviceManager(): BluetoothDeviceManager {
        return deviceManager
    }

}