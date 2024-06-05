package com.ds.highnoonblitz.bluetooth.communication_threads

import PermissionManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.ds.highnoonblitz.MainActivity
import com.ds.highnoonblitz.ToastMaker
import com.ds.highnoonblitz.bluetooth.bluetooth_management.BluetoothDeviceManager
import com.ds.highnoonblitz.bluetooth.communication_threads.Constants.BT_UUID
import com.ds.highnoonblitz.messages.MessageFactory
import java.io.IOException

@RequiresApi(Build.VERSION_CODES.S)
class ServerThread (
    private val activity: MainActivity,
    private val bluetoothAdapter: BluetoothAdapter,
    private val deviceManager: BluetoothDeviceManager,
    private val onDeviceDisconnected: (BluetoothSocket) -> Unit):
    BasicBluetoothCommunicationThread(activity, bluetoothAdapter, deviceManager) {

    private val serverSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        try {
            val discoverableIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
            }
            activity.startActivity(discoverableIntent)
            bluetoothAdapter.listenUsingRfcommWithServiceRecord("HighNoonBlitz", BT_UUID)
        } catch (e: SecurityException) {
            Log.e("ServerThread", "SecurityException while creating BluetoothServerSocket", e)
            PermissionManager.getBluetoothPermission(activity, bluetoothAdapter)
            null
        }
    }


    override fun run() {
        while(true){
            val socket: BluetoothSocket? = try {
                serverSocket?.accept()
            } catch (e: IOException) {
                Log.e("ServerThread", "Socket's accept() method failed", e)
                null
            }
            socket?.also {
                manageMyConnectedSocket(it)
               
            }
        }
    }

    private fun manageMyConnectedSocket(socket: BluetoothSocket) {
        val communicationThread = activity.getHandler()?.let { CommunicationChannelThread(socket, it, onDeviceDisconnected) }
        communicationThread!!.priority = Thread.MAX_PRIORITY

        communicationThread.start()
        if(deviceManager.isMaster()){
            val msg = MessageFactory.createConfigurationMessage(deviceManager.getMacAddresses())
            Log.i("ServerThread", "Sending config=" + msg.toString())
            communicationThread.write(msg)
        }else{
            val msg = MessageFactory.createInfoSharingMessage()
            Log.i("ServerThread", "Sending info=" + msg.toString())
            communicationThread.write(msg)
        }
        addConnection(socket, communicationThread)

//        MainThreadUtil.runOnUiThread {
//            Toast.makeText(activity, "Socket is up and running", Toast.LENGTH_SHORT).show()
//        }
        ToastMaker.makeToast("Socket is up and running!")
    }

    override fun addConnection(socket: BluetoothSocket, thread: CommunicationChannelThread) {
        super.addConnection(socket, thread)
    }
}

