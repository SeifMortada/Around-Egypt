package com.seifmortada.applications.aroundegypt.core.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.seifmortada.applications.aroundegypt.core.ui.navigation.AroundEgyptNavGraph
import com.seifmortada.applications.aroundegypt.core.ui.navigation.Destinations
import com.seifmortada.applications.aroundegypt.core.ui.theme.AroundEgyptTheme
import com.seifmortada.applications.aroundegypt.detail.presentation.DetailRoute
import com.seifmortada.applications.aroundegypt.home.presentation.HomeRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AroundEgyptTheme {
                AroundEgyptNavGraph()
            }
        }
    }
}
