package com.example.testschedule.presentation

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.testschedule.domain.model.schedule.ScheduleModel
import com.example.testschedule.presentation.account.announcement_screen.AnnouncementsScreen
import com.example.testschedule.presentation.account.dormitory_screen.DormitoryScreen
import com.example.testschedule.presentation.account.group_screen.GroupScreen
import com.example.testschedule.presentation.account.mark_book_screen.MarkBookScreen
import com.example.testschedule.presentation.account.menu_screen.AccountMenuScreen
import com.example.testschedule.presentation.account.notifications_screen.NotificationsScreen
import com.example.testschedule.presentation.account.omissions_screen.OmissionsScreen
import com.example.testschedule.presentation.account.penalty_screen.PenaltyScreen
import com.example.testschedule.presentation.account.rating_screen.RatingScreen
import com.example.testschedule.presentation.account.study_screen.StudyScreen
import com.example.testschedule.presentation.auth_screen.AuthScreen
import com.example.testschedule.presentation.schedule_screen.add_schedule_screen.AddScheduleScreen
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.exams.ViewExamsScreen
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.schedule.ViewScheduleScreen

object Routes {
    const val SCHEDULE_ROUTE = "SCHEDULE_ROUTE"
    const val SCHEDULE_HOME_ROUTE = "SCHEDULE_HOME_ROUTE/{id}/{title}/{isPreview}"
    const val SCHEDULE_EDIT_LIST_ROUTE = "SCHEDULE_EDIT_LIST_ROUTE"
    const val SCHEDULE_EXAMS_VIEW_ROUTE = "SCHEDULE_EXAMS_VIEW/{id}"

    const val LOGIN_SCREEN_ROUTE = "LOGIN_SCREEN_ROUTE"

    const val ACCOUNT_ROUTE = "ACCOUNT_ROUTE"
    const val ACCOUNT_MENU_ROUTE = "ACCOUNT_MENU_ROUTE"
    const val ACCOUNT_NOTIFICATIONS_ROUTE = "ACCOUNT_NOTIFICATIONS_ROUTE"
    const val ACCOUNT_DORMITORY_ROUTE = "ACCOUNT_DORMITORY_ROUTE"
    const val ACCOUNT_GROUP_ROUTE = "ACCOUNT_GROUP_ROUTE"
    const val ACCOUNT_MARK_BOOK_ROUTE = "ACCOUNT_MARK_BOOK_ROUTE"
    const val ACCOUNT_OMISSIONS_ROUTE = "ACCOUNT_OMISSIONS_ROUTE"
    const val ACCOUNT_PENALTY_ROUTE = "ACCOUNT_PENALTY_ROUTE"
    const val ACCOUNT_ANNOUNCEMENT_ROUTE = "ACCOUNT_ANNOUNCEMENT_ROUTE"
    const val ACCOUNT_RATING_ROUTE = "ACCOUNT_RATING_ROUTE"
    const val ACCOUNT_STUDY_ROUTE = "ACCOUNT_STUDY_ROUTE"
}

@Composable
fun NavigationScreen(
    navController: NavHostController = rememberNavController()
) {

    val examsData : MutableMap<String, ScheduleModel> = mutableMapOf()

    val enter = slideInHorizontally(
        initialOffsetX = { 450 },
        animationSpec = tween(
            durationMillis = 250,
            easing = FastOutSlowInEasing
        )
    ) + fadeIn(animationSpec = tween(250))

    val out = slideOutHorizontally(
        targetOffsetX = { 450 },
        animationSpec = tween(
            durationMillis = 250,
            easing = FastOutSlowInEasing
        )
    ) + fadeOut(animationSpec = tween(250))

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
                route = Routes.SCHEDULE_HOME_ROUTE
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
                    navToExams = { exams ->
                        examsData[exams.id] = exams
                        navController.navigate("SCHEDULE_EXAMS_VIEW/" + exams.id)
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
                route = Routes.SCHEDULE_EDIT_LIST_ROUTE,
                enterTransition = {
                    slideInVertically { pos -> pos }
                },
                exitTransition = { slideOutVertically { pos -> pos } }
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
                route = Routes.SCHEDULE_EXAMS_VIEW_ROUTE,
                enterTransition = { enter },
                exitTransition = { out }
            ) { entry ->
                val id = entry.arguments?.getString("id") ?: ""
                val exams = examsData[id]
                ViewExamsScreen(
                    exams = exams!!,
                    navBack = {
                        popNav()
                    },
                    selectScheduleClicked = { urlId, title ->
                        navController.navigate("SCHEDULE_HOME_ROUTE/${urlId}/${title}/${true}")
                    }
                )
            }
        }

        composable(
            route = Routes.LOGIN_SCREEN_ROUTE,
            enterTransition = { enter },
            exitTransition = { out }
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
            startDestination = Routes.ACCOUNT_MENU_ROUTE,
        ) {
            composable(
                route = Routes.ACCOUNT_MENU_ROUTE,
                enterTransition = {
                    fadeIn(animationSpec = tween(250))
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(250))
                }
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
                    },
                    goToAnnouncements = {
                        navController.navigate(Routes.ACCOUNT_ANNOUNCEMENT_ROUTE)
                    },
                    goToRating = {
                        navController.navigate(Routes.ACCOUNT_RATING_ROUTE)
                    },
                    goToStudy = {
                        navController.navigate(Routes.ACCOUNT_STUDY_ROUTE)
                    }
                )
            }

            composable(
                route = Routes.ACCOUNT_NOTIFICATIONS_ROUTE,
                enterTransition = { enter },
                exitTransition = { out }
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
                route = Routes.ACCOUNT_DORMITORY_ROUTE,
                enterTransition = { enter },
                exitTransition = { out }
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
                route = Routes.ACCOUNT_GROUP_ROUTE,
                enterTransition = { enter },
                exitTransition = { out }
            ) {
                GroupScreen(
                    onBackPressed = { popNav() },
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
                route = Routes.ACCOUNT_ANNOUNCEMENT_ROUTE,
                enterTransition = { enter },
                exitTransition = { out }
            ) {
                AnnouncementsScreen(
                    onBackPressed = { popNav() },
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
                route = Routes.ACCOUNT_MARK_BOOK_ROUTE,
                enterTransition = { enter },
                exitTransition = { out }
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
                route = Routes.ACCOUNT_OMISSIONS_ROUTE,
                enterTransition = { enter },
                exitTransition = { out }
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
                route = Routes.ACCOUNT_PENALTY_ROUTE,
                enterTransition = { enter },
                exitTransition = { out }
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

            composable(
                route = Routes.ACCOUNT_RATING_ROUTE,
                enterTransition = { enter },
                exitTransition = { out }
            ) {

                RatingScreen(
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
                route = Routes.ACCOUNT_STUDY_ROUTE,
                enterTransition = { enter },
                exitTransition = { out }
            ) {

                StudyScreen(
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
