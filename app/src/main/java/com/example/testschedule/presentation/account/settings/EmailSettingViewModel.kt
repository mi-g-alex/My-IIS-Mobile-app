package com.example.testschedule.presentation.account.settings

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testschedule.common.Resource
import com.example.testschedule.domain.repository.UserDatabaseRepository
import com.example.testschedule.domain.use_case.account.settings.email.EmailSettingsGetContactsUseCase
import com.example.testschedule.domain.use_case.account.settings.email.EmailSettingsUpdateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailSettingViewModel @Inject constructor(
    private val emailSettingsGetContactsUseCase: EmailSettingsGetContactsUseCase,
    private val emailSettingsUpdateUseCase: EmailSettingsUpdateUseCase,
    private val db: UserDatabaseRepository,
) : ViewModel() {

    val id = mutableIntStateOf(0)
    val email = mutableStateOf("")

    val isLoading = mutableStateOf(false)
    val errorText = mutableStateOf("")

    val isLoadingUpdate = mutableStateOf(false)

    init {
        getEmailContacts()
    }

    private fun getEmailContacts() {
        emailSettingsGetContactsUseCase().onEach { res ->

            when(res) {
                is Resource.Success -> {
                    res.data?.let {
                        if(it.contactDtoList.isNotEmpty()) {
                            id.intValue = it.contactDtoList[0].id
                            email.value = it.contactDtoList[0].contactValue
                        }
                    }
                    isLoading.value = false
                    errorText.value = ""
                }
                is Resource.Loading -> {
                    isLoading.value = true
                    errorText.value = ""
                }
                is Resource.Error -> {
                    isLoading.value = false
                    errorText.value = res.message.toString()
                }
            }

        }.launchIn(viewModelScope)
    }

    fun updateEmail(
        newEmail: String,
        onError: () -> Unit,
        onSuccess: () -> Unit
    ) {
        emailSettingsUpdateUseCase(newEmail, id.intValue).onEach { res ->

            when(res) {
                is Resource.Success -> {
                    errorText.value = ""
                    isLoadingUpdate.value = false
                    onSuccess()
                    email.value = newEmail
                }
                is Resource.Loading -> {
                    isLoadingUpdate.value = true
                }
                is Resource.Error -> {
                    isLoadingUpdate.value = false
                    errorText.value = res.message.toString()
                    onError()
                }
            }

        }.launchIn(viewModelScope)
    }

}