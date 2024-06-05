package com.ds.highnoonblitz.messages

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.annotation.RequiresApi
import com.ds.highnoonblitz.MainActivity
import com.ds.highnoonblitz.ToastMaker
import com.ds.highnoonblitz.bluetooth.BluetoothHandler
import com.ds.highnoonblitz.bluetooth.communication_threads.Purposes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class MessageHandler(private val activity: MainActivity, private val bluetoothHandler: BluetoothHandler) : Handler(
    Looper.getMainLooper()) {

    private val backgroundScope = CoroutineScope(Dispatchers.IO)

    @RequiresApi(Build.VERSION_CODES.S)
    override fun handleMessage(msg: Message) {
        val (receivedBytes, senderMacAddress) = msg.obj as Pair<ByteArray, String?>
        val receivedString = String(receivedBytes)
        when (msg.what) {
            Purposes.MESSAGE_TEST -> {
                Log.i("Client", "Received message: $receivedString")

                ToastMaker.makeToast("Message received: $receivedString")
            }

            Purposes.UPDATE_CONNECTED_LIST -> {
                backgroundScope.launch {
                    val obj = JSONObject(receivedString)
                    bluetoothHandler.connectToDevice(obj.getString("deviceMac"))
                }
            }

            Purposes.READY -> {
                backgroundScope.launch {
                    val obj = JSONObject(receivedString)
                    Log.i("Client", "Received ready message: $obj")
                    val deviceName = obj.getString("deviceName")
                    val isReady = obj.getBoolean("ready")
                    bluetoothHandler.getDeviceManager().setDeviceReadyStatus(deviceName, isReady)
                }

            }

            Purposes.CONFIGURATION -> {
                backgroundScope.launch {
                    val obj = JSONObject(receivedString)
                    if (obj.has("devices")) {
                        val devices = obj.getJSONArray("devices")
                        Log.i("Client", "Received configuration message: $devices from $senderMacAddress")
                        if (senderMacAddress != null) {
                            bluetoothHandler.getDeviceManager().setMasterAddress(senderMacAddress)
                        }
                        for (i in 0 until devices.length()) {
                            bluetoothHandler.connectToDevice(devices.getString(i))
                        }
                    }
                }

            }

            Purposes.INFO_SHARING -> {
                backgroundScope.launch {
                    val obj = JSONObject(receivedString)
                    Log.i("Client", "Received info sharing message: $obj")
                    if (obj.has("UUID")) {
                        val uuid = obj.getString("UUID")
                        Log.i("Client", "Received UUID: $uuid")
                        if (senderMacAddress != null) {
                            bluetoothHandler.getDeviceManager().addElectionListMember(senderMacAddress, uuid)
                        }
                    }
                }

            }

            Purposes.ELECTION_REQUEST -> {
                backgroundScope.launch {
                    val obj = JSONObject(receivedString)
                    Log.i("Client", "Received election request: $obj")
                    val message = MessageFactory.createAcknowledgeMessage()
                    val device = bluetoothHandler.getBluetoothAdapter()?.getRemoteDevice(senderMacAddress)
                    if (device != null) {
                        bluetoothHandler.sendMessage(message, device)
                    }
                }

            }

            Purposes.ACK -> {
                backgroundScope.launch {
                    bluetoothHandler.getDeviceManager().stopElection()
                }
            }
        }
    }
}