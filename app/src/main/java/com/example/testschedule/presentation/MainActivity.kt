package com.example.testschedule.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.ViewExamsScreen
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.ViewScheduleScreen
import com.example.testschedule.presentation.ui.theme.TestScheduleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TestScheduleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationScreen()
                }
            }
        }
    }
}

@AndroidEntryPoint
class PreviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appLinkIntent: Intent = intent
        val appLinkData: Uri? = appLinkIntent.data

        setContent {
            val navController = rememberNavController()
            fun popNav() {
                navController.popBackStack()
            }
            TestScheduleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Routes.SCHEDULE_ROUTE
                    ) {
                        composable(
                          route = Routes.SCHEDULE_ROUTE
                        ) {
                            ViewScheduleScreen(
                                goBackSet = { _, _ ->
                                },
                                scheduleId = appLinkData?.lastPathSegment ?: "",
                                titleLink = appLinkData?.lastPathSegment ?: "Плохая ссылка",
                                navToExams = {
                                    navController.navigate(Routes.SCHEDULE_EXAMS_VIEW_ROUTE)
                                },
                                goToAddSchedule = {},
                                navToLogin = {},
                                navToProfile = {},
                                isPrev = true
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
                }
            }
        }
    }
}
