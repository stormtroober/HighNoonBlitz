package com.ds.highnoonblitz.view

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ds.highnoonblitz.bluetooth.BluetoothHandler
import com.ds.highnoonblitz.view.lobbymenu.ClientLobby
import com.ds.highnoonblitz.view.lobbymenu.LobbyMenu
import com.ds.highnoonblitz.view.lobbymenu.ServerLobby

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AppScreen(activity: Activity, bluetoothHandler: BluetoothHandler){
    val navController = rememberNavController()
    ScreenWithTopBar(activity = activity) {
        NavHost(navController = navController, startDestination = AppRoutes.LOBBY_MENU.route){
            composable(AppRoutes.LOBBY_MENU.route){
                LobbyMenu(activity, bluetoothHandler, navController)
            }
            composable(AppRoutes.SERVER_LOBBY.route){
                ServerLobby(activity, bluetoothHandler, navController)
            }
            composable(AppRoutes.CLIENT_LOBBY.route){
                ClientLobby(activity, bluetoothHandler, navController)
            }
        }
    }

}
