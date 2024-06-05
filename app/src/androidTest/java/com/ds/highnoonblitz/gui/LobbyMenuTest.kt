package com.ds.highnoonblitz.gui

import android.content.res.Resources
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.ds.highnoonblitz.MainActivity
import com.ds.highnoonblitz.R
import com.ds.highnoonblitz.bluetooth.BluetoothHandler
import com.ds.highnoonblitz.bluetooth.bluetooth_management.BluetoothDeviceManager
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LobbyMenuTest {
    @get:Rule
    var composeTestRule = createAndroidComposeRule<MainActivity>()
    private var resources: Resources? = null
    private var bluetoothHandler: BluetoothHandler? = null
    @Before
    fun init(){
        bluetoothHandler = BluetoothHandler(composeTestRule.activity, deviceManager = BluetoothDeviceManager())
        resources = composeTestRule.activity.resources
    }
    @Test
    fun testCreateLobbyButton() {
        resources?.let { composeTestRule.onNodeWithText(it.getString(R.string.create_lobby)).assertIsDisplayed() }
    }
    @Test
    fun testJoinLobbyButton() {
        resources?.let { composeTestRule.onNodeWithText(it.getString(R.string.join_lobby)).assertIsDisplayed() }
    }

    @Test
    fun testNavigateToClientLobby(){
        resources?.let { composeTestRule.onNodeWithText(it.getString(R.string.join_lobby)).performClick() }
        resources?.let { composeTestRule.onNodeWithText(it.getString(R.string.search_for_lobby)).assertIsDisplayed() }
    }

    @Test
    fun testNavigateToServerLobby(){
        resources?.let { composeTestRule.onNodeWithText(it.getString(R.string.create_lobby)).performClick() }
        resources?.let { composeTestRule.onNodeWithText(it.getString(R.string.start_game)).assertIsDisplayed() }
    }


}