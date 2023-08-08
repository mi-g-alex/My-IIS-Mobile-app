package com.example.testschedule.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.testschedule.presentation.schedule_screen.add_schedule_screen.AddScheduleScreen
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.ViewExamsScreen
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.ViewScheduleScreen

object Routes {
    const val SCHEDULE_ROUTE = "SCHEDULE_ROUTE"
    const val SCHEDULE_HOME_ROUTE = "SCHEDULE_HOME_ROUTE/{id}/{title}"
    const val SCHEDULE_EDIT_LIST_ROUTE = "SCHEDULE_EDIT_LIST_ROUTE"
    const val SCHEDULE_EXAMS_VIEW = "SCHEDULE_EXAMS_VIEW"
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
            ) { entry ->
                val id = entry.savedStateHandle.get<String>("id")
                val title = entry.savedStateHandle.get<String>("title")
                ViewScheduleScreen(
                    goBackSet = { i, t ->
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("id", i)
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("title", t)
                    },
                    scheduleId = id,
                    titleLink = title,
                    navToExams = {
                        navController.navigate(Routes.SCHEDULE_EXAMS_VIEW)
                    },
                    goToAddSchedule = {
                        navController.navigate(Routes.SCHEDULE_EDIT_LIST_ROUTE)
                    }
                )
            }
            composable(
                route = Routes.SCHEDULE_EDIT_LIST_ROUTE
            ) {
                AddScheduleScreen(
                    goBack = {
                        navController.popBackStack()
                    },
                    goBackWhenSelect = { id, title ->
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("id", id)
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("title", title)
                        navController.popBackStack()
                    }
                )
            }
            composable(
                route = Routes.SCHEDULE_EXAMS_VIEW
            ) { entry ->
                ViewExamsScreen(
                    navBack = {
                        navController.popBackStack()
                    }, selectScheduleClicked = { id, title ->
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("id", id)
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("title", title)
                        navController.popBackStack()
                    })
            }
        }
    }
}
