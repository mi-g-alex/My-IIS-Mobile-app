package com.example.testschedule.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.testschedule.presentation.account.menu_screen.AccountMenuScreen
import com.example.testschedule.presentation.auth_screen.AuthScreen
import com.example.testschedule.presentation.schedule_screen.add_schedule_screen.AddScheduleScreen
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.ViewExamsScreen
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.ViewScheduleScreen

object Routes {
    const val SCHEDULE_ROUTE = "SCHEDULE_ROUTE"
    const val SCHEDULE_HOME_ROUTE = "SCHEDULE_HOME_ROUTE/{id}/{title}"
    const val SCHEDULE_EDIT_LIST_ROUTE = "SCHEDULE_EDIT_LIST_ROUTE"
    const val SCHEDULE_EXAMS_VIEW_ROUTE = "SCHEDULE_EXAMS_VIEW"

    const val LOGIN_SCREEN_ROUTE = "LOGIN_SCREEN_ROUTE"

    const val ACCOUNT_ROUTE = "ACCOUNT_ROUTE"
    const val ACCOUNT_MENU_ROUTE = "ACCOUNT_MENU_ROUTE"
}

@Composable
fun NavigationScreen(
    navController: NavHostController = rememberNavController()
) {

    fun popNav() {
        if (navController.currentBackStack.value.size > 3) navController.popBackStack()
    }

    NavHost(
        navController = navController,
        startDestination = Routes.SCHEDULE_ROUTE
    ) {
        navigation(
            route = Routes.SCHEDULE_ROUTE,
            startDestination = Routes.SCHEDULE_HOME_ROUTE
        ) {
            composable(
                route = Routes.SCHEDULE_HOME_ROUTE,
            ) { entry ->
                val id = entry.savedStateHandle.get<String>("id")
                val title = entry.savedStateHandle.get<String>("title") ?: id
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
                        navController.navigate(Routes.SCHEDULE_EXAMS_VIEW_ROUTE)
                    },
                    goToAddSchedule = {
                        navController.navigate(Routes.SCHEDULE_EDIT_LIST_ROUTE)
                    },
                    navToLogin = {
                        navController.navigate(Routes.LOGIN_SCREEN_ROUTE)
                    },
                    navToProfile = {
                        navController.navigate(Routes.ACCOUNT_ROUTE)
                    }
                )
            }
            composable(
                route = Routes.SCHEDULE_EDIT_LIST_ROUTE
            ) {
                AddScheduleScreen(
                    goBack = {
                        popNav()
                    },
                    goBackWhenSelect = { id, title ->
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("id", id)
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("title", title)
                        popNav()
                    }
                )
            }
            composable(
                route = Routes.SCHEDULE_EXAMS_VIEW_ROUTE
            ) {
                ViewExamsScreen(
                    navBack = {
                        popNav()
                    },
                    selectScheduleClicked = { id, title ->
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("id", id)
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("title", title)
                        popNav()
                    }
                )
            }
        }

        composable(
            route = Routes.LOGIN_SCREEN_ROUTE
        ) {
            AuthScreen(
                goBack = { popNav() },
                goToProfile = {
                    popNav()
                    navController.navigate(Routes.ACCOUNT_ROUTE)
                }
            )
        }

        navigation(
            route = Routes.ACCOUNT_ROUTE,
            startDestination = Routes.ACCOUNT_MENU_ROUTE
        ) {
            composable(
                route = Routes.ACCOUNT_MENU_ROUTE
            ) {
                AccountMenuScreen({
                    navController.popBackStack()
                    navController.popBackStack()
                    navController.navigate(Routes.SCHEDULE_HOME_ROUTE)
                })
            }
        }
    }
}
