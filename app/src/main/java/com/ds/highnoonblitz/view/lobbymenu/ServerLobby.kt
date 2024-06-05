package com.ds.highnoonblitz.view.lobbymenu

import android.app.Activity
import android.app.AlertDialog
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.ds.highnoonblitz.bluetooth.BluetoothHandler
import com.ds.highnoonblitz.view.components.MenuButton
import com.ds.highnoonblitz.view.components.ParticipantsList
import com.ds.highnoonblitz.view.components.StartButton

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ServerLobby(activity: Activity, bluetoothHandler: BluetoothHandler, navController: NavController){

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        MenuButton(buttonText = "Create Server", buttonAction = {
            bluetoothHandler.getDeviceManager().setMaster(true)
            bluetoothHandler.startBluetoothServer()
        })

        ParticipantsList(activity = activity, bluetoothHandler = bluetoothHandler)


        StartButton(buttonText = "Start Game", buttonAction = {
            Log.i("ServerLobby", "Game can start")

        }, bluetoothHandler)
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