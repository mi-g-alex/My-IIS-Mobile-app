package com.example.testschedule.presentation.account.rating

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testschedule.common.Resource
import com.example.testschedule.domain.model.account.penalty.PenaltyModel
import com.example.testschedule.domain.model.account.rating.RatingModel
import com.example.testschedule.domain.repository.UserDatabaseRepository
import com.example.testschedule.domain.use_case.account.penalty.GetPenaltyUseCase
import com.example.testschedule.domain.use_case.account.rating.GetRatingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RatingViewModel @Inject constructor(
    private val db: UserDatabaseRepository,
    private val getRatingUseCase: GetRatingUseCase
) : ViewModel() {

    val isLoading = mutableStateOf(false)

    val errorText = mutableStateOf("")
    val rating = mutableStateOf<RatingModel?>(null)

    init {
        getRating()
    }


    private fun getRating() {
        getRatingUseCase().onEach { res ->
            when (res) {
                is Resource.Success -> {
                    isLoading.value = false
                    res.data?.let { rating.value = it }
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