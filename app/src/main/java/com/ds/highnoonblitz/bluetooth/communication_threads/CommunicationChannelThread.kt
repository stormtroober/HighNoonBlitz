package com.ds.highnoonblitz.bluetooth.communication_threads

import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.util.Log
import com.ds.highnoonblitz.messages.MessageComposed
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.IOException

class CommunicationChannelThread (private val socket: BluetoothSocket, private val handler: Handler,
                                    private val onDeviceDisconnected: (BluetoothSocket) -> Unit) : Thread() {
    private val inputStream = BufferedInputStream(socket.inputStream)
    private val outputStream = BufferedOutputStream(socket.outputStream)

    override fun run() {
        val buffer = ByteArray(1024)
        var bytes: Int
        while (true) {
            try {
                bytes = inputStream.read(buffer)
                val readBytes = ByteArray(bytes)
                System.arraycopy(buffer, 0, readBytes, 0, bytes)
                val messageComposed = MessageComposed.Companion.MessageBuilder().fromString(String(readBytes)).build()
                this.processMessageForHandler(messageComposed)
            } catch (e: IOException) {
                Log.e("ConnectedThread", "Input stream was disconnected")
                cancel()
                break
            }
        }
    }

    private fun processMessageForHandler(messageComposed: MessageComposed) {
        val purpose = messageComposed.getPurpose()
        val message = messageComposed.getMessage().toString()
        val senderMacAddress = socket.remoteDevice.address
        handler.sendMessage(handler.obtainMessage(purpose, message.toByteArray().size, -1, Pair(message.toByteArray(), senderMacAddress)))
    }

    fun write(messageComposed: MessageComposed) {
        try {
            outputStream.write(messageComposed.toString().toByteArray())
            outputStream.flush()
        } catch (e: IOException) {
            Log.e("ConnectedThread", "Error occurred when sending data", e)
        }
    }

    fun cancel() {
        try {
            socket.close()
            onDeviceDisconnected(socket)
        } catch (e: IOException) {
            Log.e("ConnectedThread", "Could not close the connect socket", e)
        }
    }
}