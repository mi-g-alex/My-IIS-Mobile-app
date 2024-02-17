package com.example.testschedule.presentation.account.menu_screen

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testschedule.common.Resource
import com.example.testschedule.domain.model.account.profile.AccountProfileModel
import com.example.testschedule.domain.model.auth.UserBasicDataModel
import com.example.testschedule.domain.repository.UserDatabaseRepository
import com.example.testschedule.domain.use_case.account.notifications.GetNotificationsUseCase
import com.example.testschedule.domain.use_case.account.profile.GetAccountProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountProfileViewModel @Inject constructor(
    private val db: UserDatabaseRepository,
    private val getAccountProfileUseCase: GetAccountProfileUseCase,
    private val getNotificationsUseCase: GetNotificationsUseCase
) : ViewModel() {

    val isLoading = mutableStateOf(false)
    private val isLoadingAccount = mutableStateOf(false)
    private val isLoadingNotifications = mutableStateOf(false)

    val errorText = mutableStateOf("")
    val userInfo = mutableStateOf<AccountProfileModel?>(null)
    val basicInfo = mutableStateOf<UserBasicDataModel?>(null)
    val notificationsCount = mutableIntStateOf(0)

    init {
        getUserAccountInfo()
        getNotifications()
    }

    fun getUserAccountInfo() {
        isLoading.value = true
        viewModelScope.launch {
            basicInfo.value = db.getUserBasicData()
            userInfo.value = db.getAccountProfile()
        }
        getAccountProfileUseCase().onEach { res ->
            when (res) {
                is Resource.Success -> {
                    isLoadingAccount.value = false
                    isLoading.value = isLoadingAccount.value || isLoadingNotifications.value
                    userInfo.value = res.data
                    errorText.value = ""
                }

                is Resource.Error -> {
                    isLoadingAccount.value = false
                    isLoading.value = isLoadingAccount.value || isLoadingNotifications.value
                    errorText.value = res.message.toString()
                    if (errorText.value == "WrongPassword") {
                        viewModelScope.launch {
                            db.deleteUserBasicData()
                        }
                    }
                }

                is Resource.Loading -> {
                    isLoading.value = true
                    isLoadingAccount.value = true
                    errorText.value = ""
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getNotifications() {
        /*        viewModelScope.launch {
                    notificationsCount.intValue = db.getNotifications().filter { i -> !i.isViewed }.size
                }
                getNotificationsUseCase().onEach { res ->
                    when (res) {
                        is Resource.Success -> {
                            isLoadingNotifications.value = false
                            isLoading.value = isLoadingAccount.value || isLoadingNotifications.value
                            res.data?.let {
                                notificationsCount.intValue = it.filter { i -> !i.isViewed }.size
                            }
                            errorText.value = ""
                        }

                        is Resource.Error -> {
                            isLoadingNotifications.value = false
                            isLoading.value = isLoadingAccount.value || isLoadingNotifications.value
                            errorText.value = res.message.toString()
                            if (errorText.value == "WrongPassword") {
                                viewModelScope.launch {
                                    db.deleteUserBasicData()
                                }
                            }
                        }

                        is Resource.Loading -> {
                            isLoading.value = true
                            isLoadingNotifications.value = true
                            errorText.value = ""
                        }
                    }
                }.launchIn(viewModelScope)*/
    }

    fun exit() {
        viewModelScope.launch {
            db.deleteUserBasicData()
        }
    }
}