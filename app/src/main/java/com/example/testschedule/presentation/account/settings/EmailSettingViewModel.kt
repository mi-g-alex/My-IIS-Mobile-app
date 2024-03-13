package com.example.testschedule.presentation.account.settings

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testschedule.common.Resource
import com.example.testschedule.domain.repository.UserDatabaseRepository
import com.example.testschedule.domain.use_case.account.settings.email.EmailSettingsConfirmCodeUseCase
import com.example.testschedule.domain.use_case.account.settings.email.EmailSettingsGetConfirmCodeUseCase
import com.example.testschedule.domain.use_case.account.settings.email.EmailSettingsGetContactsUseCase
import com.example.testschedule.domain.use_case.account.settings.email.EmailSettingsUpdateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Calendar
import java.util.GregorianCalendar
import javax.inject.Inject

@HiltViewModel
class EmailSettingViewModel @Inject constructor(
    private val emailSettingsGetContactsUseCase: EmailSettingsGetContactsUseCase,
    private val emailSettingsUpdateUseCase: EmailSettingsUpdateUseCase,
    private val emailSettingsGetConfirmCodeUseCase: EmailSettingsGetConfirmCodeUseCase,
    private val emailSettingsConfirmCodeUseCase: EmailSettingsConfirmCodeUseCase,
) : ViewModel() {

    val id = mutableIntStateOf(0)
    val email = mutableStateOf("")

    val numberOfAttempts = mutableIntStateOf(0)
    val expiredTime = mutableLongStateOf(0L)

    val isLoading = mutableStateOf(false)
    val errorText = mutableStateOf("")

    val isLoadingForDialog = mutableStateOf(false)
    val errorTextForDialog = mutableStateOf("")

    init {
        getEmailContacts()
    }

    private fun getEmailContacts() {
        emailSettingsGetContactsUseCase().onEach { res ->

            when (res) {
                is Resource.Success -> {
                    res.data?.let {
                        if (it.contactDtoList.isNotEmpty()) {
                            id.intValue = it.contactDtoList[0].id
                            email.value = it.contactDtoList[0].contactValue
                            numberOfAttempts.intValue = it.numberOfAttempts
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

            when (res) {
                is Resource.Success -> {
                    errorTextForDialog.value = ""
                    isLoadingForDialog.value = false
                    onSuccess()
                    email.value = newEmail
                }

                is Resource.Loading -> {
                    isLoadingForDialog.value = true
                }

                is Resource.Error -> {
                    isLoadingForDialog.value = false
                    errorTextForDialog.value = res.message.toString()
                    if (errorTextForDialog.value == "WrongPassword")
                        errorText.value = "WrongPassword"
                    onError()
                }
            }

        }.launchIn(viewModelScope)
    }

    fun getCode() {
        emailSettingsGetConfirmCodeUseCase(id.intValue).onEach { res ->

            when (res) {
                is Resource.Success -> {
                    numberOfAttempts.intValue--

                    val timeStr = res.data?.codeExpiredTime.toString()

                    val reg =
                        Regex("(\\d{4})-(\\d{1,2})-(\\d{1,2})T(\\d{1,2}):(\\d{1,2}):(\\d{1,2}).+")
                    val dateArray =
                        reg.matchEntire(timeStr)?.destructured?.toList()
                            ?: emptyList()

                    try {
                        val cal = GregorianCalendar(
                            dateArray[0].toInt(),
                            dateArray[1].toInt() - 1,
                            dateArray[2].toInt(),
                            dateArray[3].toInt(),
                            dateArray[4].toInt(),
                            dateArray[5].toInt()
                        )

                        expiredTime.longValue =
                            cal.timeInMillis + Calendar.getInstance().get(Calendar.ZONE_OFFSET)
                    } catch (e: Exception) {
                        expiredTime.longValue = 0
                    }
                    isLoadingForDialog.value = false
                    errorTextForDialog.value = ""
                    return@onEach
                }

                is Resource.Loading -> {
                    isLoadingForDialog.value = true
                    errorTextForDialog.value = ""
                }

                is Resource.Error -> {
                    isLoadingForDialog.value = false
                    errorTextForDialog.value = res.message.toString()
                    if (errorTextForDialog.value == "WrongPassword")
                        errorText.value = "WrongPassword"
                }
            }

        }.launchIn(viewModelScope)
    }

    fun confirmCode(
        code: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        emailSettingsConfirmCodeUseCase(code, id.intValue).onEach { res ->

            when(res) {
                is Resource.Success -> {
                    isLoadingForDialog.value = false
                    onSuccess()
                }
                is Resource.Loading -> {
                    isLoadingForDialog.value = true
                    errorTextForDialog.value = ""
                }
                is Resource.Error -> {
                    isLoadingForDialog.value = false
                    errorTextForDialog.value = res.message.toString()
                    if (errorTextForDialog.value == "WrongPassword")
                        errorText.value = "WrongPassword"
                    onError()
                }
            }

        }.launchIn(viewModelScope)
    }

}