package com.ds.highnoonblitz.view.lobbymenu

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.ds.highnoonblitz.R
import com.ds.highnoonblitz.bluetooth.BluetoothHandler
import com.ds.highnoonblitz.view.AppRoutes
import com.ds.highnoonblitz.view.components.MenuButton

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun LobbyMenu(
    activity: Activity,
    bluetoothHandler: BluetoothHandler,
    navController: NavHostController
){
    val resources = activity.resources
    Column {
        MenuButton(buttonText = resources.getString(R.string.create_lobby),
            buttonAction = {
                navController.navigate(AppRoutes.SERVER_LOBBY.route)
            })
        MenuButton(buttonText = resources.getString(R.string.join_lobby),
            buttonAction = {
                navController.navigate(AppRoutes.CLIENT_LOBBY.route)
                bluetoothHandler.getDeviceManager().setMaster(false)
                bluetoothHandler.startBluetoothServer()
            })
    }
}

