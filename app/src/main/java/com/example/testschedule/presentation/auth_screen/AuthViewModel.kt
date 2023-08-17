package com.example.testschedule.presentation.auth_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testschedule.common.Resource
import com.example.testschedule.data.local.entity.schedule.ListOfSavedEntity
import com.example.testschedule.di.AppModule
import com.example.testschedule.domain.model.auth.UserBasicDataModel
import com.example.testschedule.domain.repository.UserDatabaseRepository
import com.example.testschedule.domain.use_case.auth.LoginToAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginToAccountUseCase: LoginToAccountUseCase,
    private val db: UserDatabaseRepository,
    private val myPreference: AppModule.MyPreference
) : ViewModel() {

    val resultData = mutableStateOf<UserBasicDataModel?>(null)
    val errorText = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val isAlreadyAdded = mutableStateOf(false)
    val isAlreadySetDefault = mutableStateOf(false)

    fun loginToAccount(username: String, password: String) {
        loginToAccountUseCase(username, password).onEach { res ->
            when (res) {
                is Resource.Success -> {
                    isLoading.value = false
                    resultData.value = res.data
                    errorText.value = ""
                }

                is Resource.Error -> {
                    isLoading.value = false
                    errorText.value = res.message.toString()
                }

                is Resource.Loading -> {
                    isLoading.value = true
                    errorText.value = ""
                }
            }
        }.launchIn(viewModelScope)
    }

    fun checkIsSaved(group: String) {
        var listOfSaved: List<ListOfSavedEntity>
        viewModelScope.launch {
            listOfSaved = db.getAllSavedScheduleList()
            isAlreadyAdded.value = listOfSaved.any { it.id == group }
            isAlreadySetDefault.value = listOfSaved.isNotEmpty()
        }
    }

    fun addGroupToSaved(group: String) {
        viewModelScope.launch {
            db.addNewSavedScheduleToList(ListOfSavedEntity(group, true, group))
        }
    }

    fun setOpenByDefault(group: String) {
        myPreference.setOpenByDefault(group, group)
    }
}