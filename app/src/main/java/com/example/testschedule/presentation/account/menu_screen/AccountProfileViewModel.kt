package com.example.testschedule.presentation.account.menu_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testschedule.common.Resource
import com.example.testschedule.domain.model.account.profile.AccountProfileModel
import com.example.testschedule.domain.repository.UserDatabaseRepository
import com.example.testschedule.domain.use_case.account.profile.GetAccountProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountProfileViewModel @Inject constructor(
    private val db: UserDatabaseRepository,
    private val getAccountProfileUseCase: GetAccountProfileUseCase
) : ViewModel() {

    val isLoading = mutableStateOf(false)
    val errorText = mutableStateOf("")
    val userInfo = mutableStateOf<AccountProfileModel?>(null)

    init {
        getUserAccountInfo()
    }

    private fun getUserAccountInfo() {
        isLoading.value = true
        getAccountProfileUseCase().onEach { res ->
            when (res) {
                is Resource.Success -> {
                    isLoading.value = false
                    userInfo.value = res.data
                    errorText.value = ""
                }

                is Resource.Error -> {
                    isLoading.value = false
                    errorText.value = res.message.toString()
                    if(errorText.value == "WrongPassword") {
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