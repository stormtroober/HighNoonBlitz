package com.ds.highnoonblitz.bluetooth.communication_threads

import PermissionManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.ds.highnoonblitz.MainActivity
import com.ds.highnoonblitz.bluetooth.bluetooth_management.BluetoothDeviceManager
import com.ds.highnoonblitz.messages.MessageComposed
import java.util.UUID

object Constants {
    val BT_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
}

object Purposes {
    const val MESSAGE_TEST: Int = 1
    const val UPDATE_CONNECTED_LIST: Int = 2
    const val READY: Int = 3
    const val CONFIGURATION: Int = 4
    const val ELECTION_REQUEST: Int = 5
    const val INFO_SHARING: Int = 6
    const val ACK : Int = 7
}

@RequiresApi(Build.VERSION_CODES.S)
open class BasicBluetoothCommunicationThread(
    activity: MainActivity,
    bluetoothAdapter: BluetoothAdapter,
    private val deviceManager: BluetoothDeviceManager
) : Thread(){
    init {
        if( !PermissionManager.hasAllPermissions(activity)){
            PermissionManager.getBluetoothPermission(activity, bluetoothAdapter)
        }
    }

    @Synchronized
    open fun addConnection(socket: BluetoothSocket, thread: CommunicationChannelThread) {
        deviceManager.addNewConnection(socket, thread)
    }

    fun sendBroadcastMessage(message: MessageComposed) {

        deviceManager.getAllConnectedSockets().forEach { socket ->
            try{
                Log.i("Server", socket.first.remoteDevice.name)
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
            socket.second.write(message)
        }
    }



    fun sendMessage(device: BluetoothDevice, message: MessageComposed) {
        deviceManager.getSpecificConnectedSocket(device)!!.second.write(message)
    }


}