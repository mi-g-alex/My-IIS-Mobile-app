package com.example.testschedule.presentation.account.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testschedule.common.Resource
import com.example.testschedule.domain.model.account.profile.AccountProfileModel
import com.example.testschedule.domain.model.auth.UserBasicDataModel
import com.example.testschedule.domain.repository.UserDatabaseRepository
import com.example.testschedule.domain.use_case.account.profile.GetAccountProfileUseCase
import com.example.testschedule.domain.use_case.account.settings.UpdateSettingsBioUseCase
import com.example.testschedule.domain.use_case.account.settings.UpdateSettingsPasswordUseCase
import com.example.testschedule.domain.use_case.account.settings.UpdateSettingsViewUseCase
import com.example.testschedule.presentation.account.settings.additional.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getAccountProfileUseCase: GetAccountProfileUseCase,
    private val updateSettingsViewUseCase: UpdateSettingsViewUseCase,
    private val updatePasswordUseCase: UpdateSettingsPasswordUseCase,
    private val updateSettingsBioUseCase: UpdateSettingsBioUseCase,
    private val db: UserDatabaseRepository,
) : ViewModel() {

    val isLoading = mutableStateOf(false)
    val errorText = mutableStateOf("")
    val isLoadingPass = mutableStateOf(false)
    val errorPassText = mutableStateOf("")
    val isLoadingBio = mutableStateOf(false)
    val errorBioText = mutableStateOf("")

    val userInfo = mutableStateOf<AccountProfileModel?>(null)
    val basicInfo = mutableStateOf<UserBasicDataModel?>(null)

    val canViewProfile = mutableStateOf(false)
    val canViewRating = mutableStateOf(false)
    val canSearchJob = mutableStateOf(false)

    val selectedDialog = mutableStateOf(DialogType.NONE)

    init {
        getUserAccountInfo()
    }

    private fun getUserAccountInfo() {
        isLoading.value = true
        viewModelScope.launch {
            basicInfo.value = db.getUserBasicData()
            userInfo.value = db.getAccountProfile()
            canViewProfile.value = userInfo.value?.settingPublished ?: false
            canViewRating.value = userInfo.value?.settingShowRating ?: false
            canSearchJob.value = userInfo.value?.settingSearchJob ?: false
        }
        getAccountProfileUseCase().onEach { res ->
            when (res) {
                is Resource.Success -> {
                    isLoading.value = false
                    userInfo.value = res.data
                    errorText.value = ""
                    canViewProfile.value = userInfo.value?.settingPublished ?: false
                    canViewRating.value = userInfo.value?.settingShowRating ?: false
                    canSearchJob.value = userInfo.value?.settingSearchJob ?: false
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

    fun updateViewInfo(
        viewProfile: Boolean? = null,
        viewRating: Boolean? = null,
        viewJob: Boolean? = null,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        updateSettingsViewUseCase(
            viewProfile = viewProfile,
            viewRating = viewRating,
            viewJob = viewJob
        ).onEach { res ->
            when (res) {
                is Resource.Success -> {
                    onSuccess()
                    isLoading.value = false
                }

                is Resource.Loading -> {
                    isLoading.value = true
                }

                is Resource.Error -> {
                    onError()
                    isLoading.value = false
                    errorText.value = res.message.toString()
                }
            }
        }.launchIn(viewModelScope)
    }

    fun updatePassword(
        oldPassword: String,
        newPassword: String,
        onSuccess: () -> Unit,
        onWrongOldPassword: () -> Unit,
        onError: (Boolean) -> Unit
    ) {
        updatePasswordUseCase(
            oldPassword, newPassword
        ).onEach { res ->
            when (res) {
                is Resource.Success -> {
                    onSuccess()
                    isLoadingPass.value = false
                }

                is Resource.Loading -> {
                    isLoadingPass.value = true
                }

                is Resource.Error -> {
                    if (res.message.toString() == "WrongOldPassword")
                        onWrongOldPassword()
                    else if (res.message.toString() == "WrongPassword")
                        errorText.value = res.message.toString()
                    else
                        onError(res.message.toString() == "ConnectionFailed")

                    isLoadingPass.value = false
                    errorPassText.value = res.message.toString()
                }
            }
        }.launchIn(viewModelScope)
    }

    fun updateBio(
        bio: String,
        onSuccess: () -> Unit,
        onError: (Boolean) -> Unit
    ) {
        updateSettingsBioUseCase(bio).onEach { res ->
            when (res) {
                is Resource.Success -> {
                    onSuccess()
                    userInfo.value = userInfo.value?.copy(bio = bio)
                    isLoadingBio.value = false
                }

                is Resource.Loading -> {
                    isLoadingBio.value = true
                }
                is Resource.Error -> {
                    if (res.message.toString() == "WrongPassword")
                        errorText.value = res.message.toString()
                    else
                        onError(res.message.toString() == "ConnectionFailed")

                    isLoadingBio.value = false
                    errorPassText.value = res.message.toString()
                }
            }
        }.launchIn(viewModelScope)
    }

}