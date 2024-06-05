package com.ds.highnoonblitz

import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.ds.highnoonblitz.bluetooth.BluetoothHandler
import com.ds.highnoonblitz.messages.MessageHandler
import com.ds.highnoonblitz.view.AppScreen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    private var messageHandler: Handler? = null
    private var bluetoothHandler: BluetoothHandler? = null
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bluetoothHandler = BluetoothHandler(this)
        messageHandler = MessageHandler(this, bluetoothHandler!!)
        setContent {
            AppScreen(this, bluetoothHandler!!)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun getHandler(): Handler? {
        return messageHandler
    }
}