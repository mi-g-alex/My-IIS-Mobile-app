package com.example.testschedule.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.testschedule.presentation.account.dormitory_screen.DormitoryScreen
import com.example.testschedule.presentation.account.group_screen.GroupScreen
import com.example.testschedule.presentation.account.mark_book_screen.MarkBookScreen
import com.example.testschedule.presentation.account.menu_screen.AccountMenuScreen
import com.example.testschedule.presentation.account.notifications_screen.NotificationsScreen
import com.example.testschedule.presentation.account.omissions_screen.OmissionsScreen
import com.example.testschedule.presentation.account.penalty_screen.PenaltyScreen
import com.example.testschedule.presentation.auth_screen.AuthScreen
import com.example.testschedule.presentation.schedule_screen.add_schedule_screen.AddScheduleScreen
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.ViewExamsScreen
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.ViewScheduleScreen

object Routes {
    const val SCHEDULE_ROUTE = "SCHEDULE_ROUTE"
    const val SCHEDULE_HOME_ROUTE = "SCHEDULE_HOME_ROUTE/{id}/{title}/{isPreview}"
    const val SCHEDULE_EDIT_LIST_ROUTE = "SCHEDULE_EDIT_LIST_ROUTE"
    const val SCHEDULE_EXAMS_VIEW_ROUTE = "SCHEDULE_EXAMS_VIEW"

    const val LOGIN_SCREEN_ROUTE = "LOGIN_SCREEN_ROUTE"

    const val ACCOUNT_ROUTE = "ACCOUNT_ROUTE"
    const val ACCOUNT_MENU_ROUTE = "ACCOUNT_MENU_ROUTE"
    const val ACCOUNT_NOTIFICATIONS_ROUTE = "ACCOUNT_NOTIFICATIONS_ROUTE"
    const val ACCOUNT_DORMITORY_ROUTE = "ACCOUNT_DORMITORY_ROUTE"
    const val ACCOUNT_GROUP_ROUTE = "ACCOUNT_GROUP_ROUTE"
    const val ACCOUNT_MARK_BOOK_ROUTE = "ACCOUNT_MARK_BOOK_ROUTE"
    const val ACCOUNT_OMISSIONS_ROUTE = "ACCOUNT_OMISSIONS_ROUTE"
    const val ACCOUNT_PENALTY_ROUTE = "ACCOUNT_PENALTY_ROUTE"
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
                var id = entry.savedStateHandle.get<String>("id")
                var title = entry.savedStateHandle.get<String>("title") ?: id

                val isPreview = entry.arguments?.getString("isPreview") ?: "false"
                if (isPreview == "true") {
                    id = entry.arguments?.getString("id") ?: id
                    title = entry.arguments?.getString("title") ?: title
                }
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
                    },
                    isPrev = isPreview == "true",
                    goToPreview = { myId, myTitle ->
                        navController.navigate("SCHEDULE_HOME_ROUTE/${myId}/${myTitle}/${true}")
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
                    selectScheduleClicked = {  urlId, title ->
                            navController.navigate("SCHEDULE_HOME_ROUTE/${urlId}/${title}/${true}")
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



        // MENU -----------------------------------------------------------------------------------
        navigation(
            route = Routes.ACCOUNT_ROUTE,
            startDestination = Routes.ACCOUNT_MENU_ROUTE
        ) {
            composable(
                route = Routes.ACCOUNT_MENU_ROUTE
            ) {
                AccountMenuScreen(
                    goBack = {
                        navController.popBackStack()
                        navController.popBackStack()
                        navController.navigate(Routes.SCHEDULE_HOME_ROUTE)
                    },
                    goToNotifications = {
                        navController.navigate(Routes.ACCOUNT_NOTIFICATIONS_ROUTE)
                    },
                    goToDormitory = {
                        navController.navigate(Routes.ACCOUNT_DORMITORY_ROUTE)
                    },
                    goToGroup = {
                        navController.navigate(Routes.ACCOUNT_GROUP_ROUTE)
                    },
                    goToMarkBook = {
                        navController.navigate(Routes.ACCOUNT_MARK_BOOK_ROUTE)
                    },
                    goToOmissions = {
                        navController.navigate(Routes.ACCOUNT_OMISSIONS_ROUTE)
                    },
                    goToPenalty = {
                        navController.navigate(Routes.ACCOUNT_PENALTY_ROUTE)
                    }
                )
            }

            composable(
                route = Routes.ACCOUNT_NOTIFICATIONS_ROUTE
            ) {
                NotificationsScreen(
                    onBackPressed = {
                        popNav()
                    },
                    onLogOut = {
                        navController.popBackStack()
                        navController.popBackStack()
                        navController.popBackStack()
                        navController.navigate(Routes.SCHEDULE_HOME_ROUTE)
                    }
                )
            }

            composable(
                route = Routes.ACCOUNT_DORMITORY_ROUTE
            ) {
                DormitoryScreen(
                    onBackPressed = {
                        popNav()
                    },
                    onLogOut = {
                        navController.popBackStack()
                        navController.popBackStack()
                        navController.popBackStack()
                        navController.navigate(Routes.SCHEDULE_HOME_ROUTE)
                    }
                )
            }

            composable(
                route = Routes.ACCOUNT_GROUP_ROUTE
            ) {
                GroupScreen(onBackPressed = { popNav() },
                    onLogOut = {
                        navController.popBackStack()
                        navController.popBackStack()
                        navController.popBackStack()
                        navController.navigate(Routes.SCHEDULE_HOME_ROUTE)
                    }, goToSchedule = { urlId, title ->
                        navController.navigate("SCHEDULE_HOME_ROUTE/${urlId}/${title}/${true}")
                    }
                )
            }

            composable(
                route = Routes.ACCOUNT_MARK_BOOK_ROUTE
            ) {
                MarkBookScreen(
                    onBackPressed = { popNav() },
                    onLogOut = {
                        navController.popBackStack()
                        navController.popBackStack()
                        navController.popBackStack()
                        navController.navigate(Routes.SCHEDULE_HOME_ROUTE)
                    }
                )
            }

            composable(
                route = Routes.ACCOUNT_OMISSIONS_ROUTE
            ) {
                OmissionsScreen(
                    onBackPressed = { popNav() },
                    onLogOut = {
                        navController.popBackStack()
                        navController.popBackStack()
                        navController.popBackStack()
                        navController.navigate(Routes.SCHEDULE_HOME_ROUTE)
                    }
                )
            }

            composable(
                route = Routes.ACCOUNT_PENALTY_ROUTE
            ) {
                PenaltyScreen(
                    onBackPressed = { popNav() },
                    onLogOut = {
                        navController.popBackStack()
                        navController.popBackStack()
                        navController.popBackStack()
                        navController.navigate(Routes.SCHEDULE_HOME_ROUTE)
                    }
                )
            }
        }
    }
}
