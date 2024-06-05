package com.ds.highnoonblitz.bluetooth.bluetooth_management

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.ds.highnoonblitz.bluetooth.communication_threads.CommunicationChannelThread
import com.ds.highnoonblitz.leader_election_algorithm.BullyElectionManager

class BluetoothDeviceManager(private val electionManager: BullyElectionManager) {
    private val allConnectedSockets = mutableStateListOf<Pair<BluetoothSocket, CommunicationChannelThread>>()
    private val readyDevices = mutableStateListOf<String>()
    private var isLobbyMaster = false
    private var masterAddress = mutableStateOf("")


    @Synchronized
    fun setDeviceReadyStatus(deviceName: String, isReady: Boolean) {
        if (isReady) {
            readyDevices.add(deviceName)
        } else {
            readyDevices.remove(deviceName)
        }
    }
    fun isLobbyReady(): Boolean {
        return readyDevices.size == allConnectedSockets.size && allConnectedSockets.isNotEmpty()
    }
    fun getAllReadyDevices(): SnapshotStateList<String> {
        return readyDevices
    }
    fun getAllConnectedSockets(): SnapshotStateList<Pair<BluetoothSocket, CommunicationChannelThread>> {
        return allConnectedSockets
    }

    fun getSpecificConnectedSocket(device: BluetoothDevice): Pair<BluetoothSocket, CommunicationChannelThread>? {
        return allConnectedSockets.find { pair -> pair.first.remoteDevice == device }
    }

    fun disconnectAll(){
        allConnectedSockets.forEach { pair ->
            pair.first.close()
        }
        allConnectedSockets.clear()
    }
    @SuppressLint("MissingPermission")
    fun removeSocket(socket: BluetoothSocket){
        allConnectedSockets.removeIf { pair -> pair.first == socket }
        readyDevices.removeIf { deviceName -> deviceName == socket.remoteDevice.name }
        electionManager.removeElectionListMember(socket.remoteDevice.address)
    }

    fun isMaster(): Boolean{
        return isLobbyMaster
    }

    fun setMaster(value: Boolean){
        isLobbyMaster = value
    }

    fun isDeviceAlreadyConnected(device: BluetoothDevice): Boolean{
        return allConnectedSockets.any { pair -> pair.first.remoteDevice == device }
    }

    fun addNewConnection(socket: BluetoothSocket, communicationChannelThread: CommunicationChannelThread){
        allConnectedSockets.add(Pair(socket, communicationChannelThread))
    }

    fun getMacAddresses(): List<String> {
        return allConnectedSockets.map { it.first.remoteDevice.address }
    }

    fun setMasterAddress(masterAddress: String) {
        this.masterAddress.value = masterAddress
    }

    fun getMasterAddress(): MutableState<String> {
        return masterAddress
    }


    fun addElectionListMember(deviceAddress: String, uuid: String) {
        electionManager.addElectionListMember(deviceAddress, uuid)
    }

    fun startElection() {
        electionManager.startElectionTimer()
    }

    fun stopElection() {
        electionManager.stopElectionTimer()
    }

    fun getElectionList(): List<String> {
        return electionManager.getDevicesToElect()
    }





}