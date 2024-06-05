package com.ds.highnoonblitz.view.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DiscoveredDeviceButton(buttonText: String, buttonAction: () -> Unit,
                           buttonColor: Color = MaterialTheme.colorScheme.secondary) {
    Button(modifier = Modifier
        .padding(2.dp)
        .size(300.dp, 40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor),
            onClick = buttonAction){
        Text(text = buttonText)
    }
}