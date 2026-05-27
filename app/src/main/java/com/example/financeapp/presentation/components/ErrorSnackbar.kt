package com.example.financeapp.presentation.components

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ErrorSnackbarHost(snackbarHostState: SnackbarHostState) {
    SnackbarHost(hostState = snackbarHostState) { data ->
        Snackbar(
            containerColor = Color(0xFFB71C1C),
            contentColor   = Color.White,
            action = {
                TextButton(onClick = { data.dismiss() }) {
                    Text("ОК", color = Color.White)
                }
            }
        ) {
            Text(data.visuals.message)
        }
    }
}