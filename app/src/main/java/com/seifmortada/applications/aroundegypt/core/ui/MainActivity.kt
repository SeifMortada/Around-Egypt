package com.seifmortada.applications.aroundegypt.core.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.seifmortada.applications.aroundegypt.core.ui.navigation.Destinations
import com.seifmortada.applications.aroundegypt.core.ui.theme.AroundEgyptTheme
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

@Composable
fun AroundEgyptNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Destinations.HOME,
        modifier = modifier
    ) {
        composable<Destinations.HOME> {
            HomeRoute(
                onExperienceClick = { experienceId ->
                    navController.navigate(Destinations.DETAILS(experienceId))
                }
            )
        }
        composable<Destinations.DETAILS> {  }
    }
}