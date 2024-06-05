package com.ds.highnoonblitz.view.lobbymenu

import PermissionManager
import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothDevice
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ds.highnoonblitz.R
import com.ds.highnoonblitz.bluetooth.BluetoothHandler
import com.ds.highnoonblitz.view.components.DiscoveredDeviceButton
import com.ds.highnoonblitz.view.components.MenuButton
import com.ds.highnoonblitz.view.components.ParticipantsList
import com.ds.highnoonblitz.view.components.ReadyButton

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ClientLobby(activity: Activity, bluetoothHandler: BluetoothHandler, navController: NavController){

    val discoveredDevices = remember { mutableStateListOf<BluetoothDevice>() }


    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        if(bluetoothHandler.getDeviceManager().getAllConnectedSockets().isEmpty()) {
            MenuButton(buttonText = activity.getString(R.string.search_for_lobby), buttonAction = {
                bluetoothHandler.startBluetoothClientAndDiscovery(discoveredDevices)
            })
            LazyColumn(
                modifier = Modifier.height(250.dp)
            ) {
                items(discoveredDevices) { device ->
                    var deviceName = "Unknown device"
                    try {
                        deviceName = device.name ?: "Unknown device"
                    } catch (e: SecurityException) {
                        bluetoothHandler.getBluetoothAdapter()?.let {
                            PermissionManager.getBluetoothPermission(activity, it)
                        }
                    }
                    if(deviceName != "Unknown device"){
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            DiscoveredDeviceButton(buttonText = deviceName, buttonAction = {
                                bluetoothHandler.connectToDevice(device.address)
                            })
                        }
                    }

                }
            }

        }else{
                ParticipantsList(
                    activity = activity,
                    bluetoothHandler = bluetoothHandler
                )
                ReadyButton(activity = activity, bluetoothHandler = bluetoothHandler)
            }
        }
    // Handle back press
    BackHandler(onBack = {
        if (bluetoothHandler.getDeviceManager().getAllConnectedSockets().isNotEmpty()) {
            // Show dialog
            AlertDialog.Builder(activity)
                .setTitle("Exit Lobby")
                .setMessage("Do you want to exit the lobby?")
                .setPositiveButton("Yes") { dialog, which ->
                    bluetoothHandler.getDeviceManager().disconnectAll()


                    // Navigate back
                    navController.popBackStack()
                }
                .setNegativeButton("No", null)
                .show()
        } else {
            navController.popBackStack()
        }
    })



}