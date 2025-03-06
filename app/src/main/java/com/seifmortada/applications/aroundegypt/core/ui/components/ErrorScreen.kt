package com.seifmortada.applications.aroundegypt.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
 fun ErrorScreen(errorState: String) {
    Box {
        Text(text = "Error: $errorState")
    }
}