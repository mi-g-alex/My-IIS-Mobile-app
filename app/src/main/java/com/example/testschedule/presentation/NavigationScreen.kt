package com.example.testschedule.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.ViewScheduleScreen

object Routes {
    const val SCHEDULE_ROUTE = "SCHEDULE_HOME_ROUTE"
    const val SCHEDULE_HOME_ROUTE = "SCHEDULE_HOME_ROUTE/{id}"
    const val SCHEDULE_EDIT_LIST_ROUTE = "SCHEDULE_EDIT_LIST_ROUTE"
}

@Composable
fun NavigationScreen(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SCHEDULE_ROUTE
    ) {
        navigation(
            route = Routes.SCHEDULE_ROUTE,
            startDestination = Routes.SCHEDULE_HOME_ROUTE
        ) {
            composable(
                route = Routes.SCHEDULE_HOME_ROUTE
            ) {
                ViewScheduleScreen()
            }
            composable(
                route = Routes.SCHEDULE_EDIT_LIST_ROUTE
            ) {

            }
        }
    }
}
