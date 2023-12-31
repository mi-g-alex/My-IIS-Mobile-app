package com.example.testschedule.presentation.account.notifications_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testschedule.common.Resource
import com.example.testschedule.domain.model.account.notifications.NotificationModel
import com.example.testschedule.domain.repository.UserDatabaseRepository
import com.example.testschedule.domain.use_case.account.notifications.GetNotificationsUseCase
import com.example.testschedule.domain.use_case.account.notifications.ReadNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val db: UserDatabaseRepository,
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val readNotificationsUseCase: ReadNotificationsUseCase
) : ViewModel() {

    val isLoading = mutableStateOf(false)

    val errorText = mutableStateOf("")
    val notifications = mutableStateOf<List<NotificationModel>>(emptyList())

    init {
        getNotifications()
    }


    private fun getNotifications() {
        viewModelScope.launch {
            notifications.value = db.getNotifications()
            val listOfUnread = notifications.value.filter { !it.isViewed }.map { it.id }
            db.updateNotificationStatus(listOfUnread)
        }
        getNotificationsUseCase().onEach { res ->
            when (res) {
                is Resource.Success -> {
                    isLoading.value = false
                    res.data?.let { notifications.value = it }
                    val listOfUnread = notifications.value.filter { !it.isViewed }.map { it.id }
                    viewModelScope.launch {
                        db.updateNotificationStatus(listOfUnread)
                    }
                    readNotificationsUseCase(listOfUnread).launchIn(viewModelScope)
                    errorText.value = ""
                }

                is Resource.Error -> {
                    isLoading.value = false
                    errorText.value = res.message.toString()
                    if (errorText.value == "WrongPassword") {
                        viewModelScope.launch {
                            db.deleteUserBasicData()
                        }
                    }
                }

                is Resource.Loading -> {
                    isLoading.value = true
                    errorText.value = ""
                }
            }
        }.launchIn(viewModelScope)
    }
}