package com.example.testschedule.presentation.account.dormitory_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testschedule.common.Resource
import com.example.testschedule.domain.model.account.dormitory.DormitoryModel
import com.example.testschedule.domain.model.account.dormitory.PrivilegesModel
import com.example.testschedule.domain.repository.UserDatabaseRepository
import com.example.testschedule.domain.use_case.account.dormitory.GetDormitoryUseCase
import com.example.testschedule.domain.use_case.account.dormitory.GetPrivilegesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DormitoryViewModel @Inject constructor(
    private val db: UserDatabaseRepository,
    private val getDormitoryUseCase: GetDormitoryUseCase,
    private val getPrivilegesUseCase: GetPrivilegesUseCase
) : ViewModel() {

    val isLoading = mutableStateOf(false)
    private val isDormitoryLoading = mutableStateOf(false)
    private val isPrivilegesLoading = mutableStateOf(false)
    val errorText = mutableStateOf("")

    val dormitory = mutableStateOf<List<DormitoryModel>?>(null)
    val privileges = mutableStateOf<List<PrivilegesModel>?>(null)

    init {
        getDormitory()
        getPrivileges()
    }


    private fun getDormitory() {
        getDormitoryUseCase().onEach { res ->
            when (res) {
                is Resource.Success -> {
                    isDormitoryLoading.value = false
                    isLoading.value = isDormitoryLoading.value || isPrivilegesLoading.value
                    res.data?.let { dormitory.value = it }
                    errorText.value = ""
                }

                is Resource.Error -> {
                    isDormitoryLoading.value = false
                    isLoading.value = isDormitoryLoading.value || isPrivilegesLoading.value
                    errorText.value = res.message.toString()
                    if (errorText.value == "WrongPassword") {
                        viewModelScope.launch {
                            db.deleteUserBasicData()
                        }
                    }
                }

                is Resource.Loading -> {
                    isLoading.value = true
                    isDormitoryLoading.value = true
                    errorText.value = ""
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getPrivileges() {
        getPrivilegesUseCase().onEach { res ->
            when (res) {
                is Resource.Success -> {
                    isPrivilegesLoading.value = false
                    isLoading.value = isDormitoryLoading.value || isPrivilegesLoading.value
                    res.data?.let { privileges.value = it }
                    errorText.value = ""
                }

                is Resource.Error -> {
                    isPrivilegesLoading.value = false
                    isLoading.value = isDormitoryLoading.value || isPrivilegesLoading.value
                    errorText.value = res.message.toString()
                    if (errorText.value == "WrongPassword") {
                        viewModelScope.launch {
                            db.deleteUserBasicData()
                        }
                    }
                }

                is Resource.Loading -> {
                    isLoading.value = true
                    isPrivilegesLoading.value = true
                    errorText.value = ""
                }
            }
        }.launchIn(viewModelScope)
    }
}