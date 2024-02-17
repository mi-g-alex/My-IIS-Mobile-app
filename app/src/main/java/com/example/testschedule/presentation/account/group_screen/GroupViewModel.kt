package com.example.testschedule.presentation.account.group_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testschedule.common.Resource
import com.example.testschedule.domain.model.account.group.GroupModel
import com.example.testschedule.domain.repository.UserDatabaseRepository
import com.example.testschedule.domain.use_case.account.group.GetGroupListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val db: UserDatabaseRepository,
    private val getGroupListUseCase: GetGroupListUseCase
) : ViewModel() {

    val isLoading = mutableStateOf(false)
    val errorText = mutableStateOf("")

    val group = mutableStateOf<GroupModel?>(null)

    init {
        getGroup()
    }


    private fun getGroup() {
        viewModelScope.launch {
            group.value = db.getUserGroup()
        }
        getGroupListUseCase().onEach { res ->
            when (res) {
                is Resource.Success -> {
                    isLoading.value = false
                    group.value = res.data
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