package com.example.testschedule.presentation.account.penalty_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testschedule.common.Resource
import com.example.testschedule.domain.model.account.notifications.NotificationModel
import com.example.testschedule.domain.model.account.penalty.PenaltyModel
import com.example.testschedule.domain.repository.UserDatabaseRepository
import com.example.testschedule.domain.use_case.account.notifications.GetNotificationsUseCase
import com.example.testschedule.domain.use_case.account.notifications.ReadNotificationsUseCase
import com.example.testschedule.domain.use_case.account.penalty.GetPenaltyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PenaltyViewModel @Inject constructor(
    private val db: UserDatabaseRepository,
    private val getPenaltyUseCase: GetPenaltyUseCase
) : ViewModel() {

    val isLoading = mutableStateOf(false)

    val errorText = mutableStateOf("")
    val penalty = mutableStateOf<List<PenaltyModel>>(emptyList())

    init {
        getPenalty()
    }


    private fun getPenalty() {
        viewModelScope.launch {
            penalty.value = db.getPenalty()
        }
        getPenaltyUseCase().onEach { res ->
            when (res) {
                is Resource.Success -> {
                    isLoading.value = false
                    res.data?.let { penalty.value = it }
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