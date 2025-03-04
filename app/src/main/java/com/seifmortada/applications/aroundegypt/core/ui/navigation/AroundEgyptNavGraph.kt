package com.seifmortada.applications.aroundegypt.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.seifmortada.applications.aroundegypt.detail.presentation.DetailRoute
import com.seifmortada.applications.aroundegypt.home.presentation.HomeRoute

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
        composable<Destinations.DETAILS> {
            val args = it.toRoute<Destinations.DETAILS>()
            DetailRoute(
                experienceId = args.experienceId
            )
        }
    }
}