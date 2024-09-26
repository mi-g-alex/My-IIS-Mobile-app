package com.example.testschedule.presentation.account.settings

import android.graphics.Bitmap
import android.util.Base64
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testschedule.common.Resource
import com.example.testschedule.domain.model.account.profile.AccountProfileModel
import com.example.testschedule.domain.model.auth.UserBasicDataModel
import com.example.testschedule.domain.repository.UserDatabaseRepository
import com.example.testschedule.domain.use_case.account.profile.GetAccountProfileUseCase
import com.example.testschedule.domain.use_case.account.settings.UpdateSettingsBioUseCase
import com.example.testschedule.domain.use_case.account.settings.UpdateSettingsImageUseCase
import com.example.testschedule.domain.use_case.account.settings.UpdateSettingsLinksUseCase
import com.example.testschedule.domain.use_case.account.settings.UpdateSettingsPasswordUseCase
import com.example.testschedule.domain.use_case.account.settings.UpdateSettingsSkillsUseCase
import com.example.testschedule.domain.use_case.account.settings.UpdateSettingsViewUseCase
import com.example.testschedule.presentation.account.settings.additional.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getAccountProfileUseCase: GetAccountProfileUseCase,
    private val updateSettingsViewUseCase: UpdateSettingsViewUseCase,
    private val updatePasswordUseCase: UpdateSettingsPasswordUseCase,
    private val updateSettingsBioUseCase: UpdateSettingsBioUseCase,
    private val updateSettingsSkillsUseCase: UpdateSettingsSkillsUseCase,
    private val updateSettingsLinksUseCase: UpdateSettingsLinksUseCase,
    private val updateSettingsImageUseCase: UpdateSettingsImageUseCase,
    private val db: UserDatabaseRepository,
) : ViewModel() {

    val isLoading = mutableStateOf(false)
    val errorText = mutableStateOf("")
    val isLoadingPass = mutableStateOf(false)
    val errorPassText = mutableStateOf("")
    val isLoadingBio = mutableStateOf(false)
    val errorBioText = mutableStateOf("")
    val isLoadingSkills = mutableStateOf(false)
    val errorSkillsText = mutableStateOf("")
    val isLoadingLinks = mutableStateOf(false)
    val errorLinksText = mutableStateOf("")

    val userInfo = mutableStateOf<AccountProfileModel?>(null)
    val basicInfo = mutableStateOf<UserBasicDataModel?>(null)

    val canViewProfile = mutableStateOf(false)
    val canViewRating = mutableStateOf(false)
    val canSearchJob = mutableStateOf(false)

    val selectedDialog = mutableStateOf(DialogType.NONE)

    init {
        getUserAccountInfo()
    }

    fun updateEmail(mail: String) {
        viewModelScope.launch {
            basicInfo.value = basicInfo.value?.copy(email = mail, hasNotConfirmedContact = true)
            basicInfo.value?.let { db.setUserBasicData(it) }
        }
    }

    fun confirmedEmail() {
        viewModelScope.launch {
            basicInfo.value = basicInfo.value?.copy(hasNotConfirmedContact = false)
            basicInfo.value?.let { db.setUserBasicData(it) }
        }
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

    fun updateSkills(
        skills: List<AccountProfileModel.SkillModel>,
        onSuccess: () -> Unit,
        onError: (Boolean) -> Unit
    ) {
        updateSettingsSkillsUseCase(skills).onEach { res ->
            when (res) {
                is Resource.Success -> {
                    onSuccess()
                    userInfo.value = userInfo.value?.copy(skills = skills)
                    viewModelScope.launch { userInfo.value?.let { db.setAccountProfile(it) } }
                    isLoadingSkills.value = false
                }

                is Resource.Loading -> {
                    isLoadingSkills.value = true
                }

                is Resource.Error -> {
                    if (res.message.toString() == "WrongPassword")
                        errorText.value = res.message.toString()
                    else
                        onError(res.message.toString() == "ConnectionFailed")

                    isLoadingSkills.value = false
                    errorSkillsText.value = res.message.toString()
                }
            }
        }.launchIn(viewModelScope)
    }

    fun updateLinks(
        links: List<AccountProfileModel.ReferenceModel>,
        onSuccess: () -> Unit,
        onError: (Boolean) -> Unit
    ) {
        updateSettingsLinksUseCase(links).onEach { res ->
            when (res) {
                is Resource.Success -> {
                    onSuccess()
                    userInfo.value = userInfo.value?.copy(references = links)
                    viewModelScope.launch { userInfo.value?.let { db.setAccountProfile(it) } }
                    isLoadingLinks.value = false
                }

                is Resource.Loading -> {
                    isLoadingLinks.value = true
                }

                is Resource.Error -> {
                    if (res.message.toString() == "WrongPassword")
                        errorText.value = res.message.toString()
                    else
                        onError(res.message.toString() == "ConnectionFailed")

                    isLoadingLinks.value = false
                    errorLinksText.value = res.message.toString()
                }
            }
        }.launchIn(viewModelScope)
    }

    fun updatePhoto(
        bt: Bitmap
    ) {
        var s: String
        viewModelScope.launch {
            try {
                val baos = ByteArrayOutputStream()
                bt.compress(Bitmap.CompressFormat.JPEG, 10, baos)
                val b = baos.toByteArray()
                s = Base64.encodeToString(b, Base64.NO_WRAP)
            } catch (e: Exception) {
                return@launch
            }
            updateSettingsImageUseCase(s).onEach { res ->

                when (res) {
                    is Resource.Success -> {
                        userInfo.value = userInfo.value?.copy(photoUrl = res.data.toString())
                    }

                    is Resource.Loading -> {}
                    is Resource.Error -> {
                        errorText.value = res.message.toString()
                    }
                }

            }.launchIn(viewModelScope)

        }
    }

}