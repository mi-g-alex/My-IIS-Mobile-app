package com.example.testschedule.presentation.account.study_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testschedule.common.Resource
import com.example.testschedule.domain.model.account.study.certificate.CertificateModel
import com.example.testschedule.domain.repository.UserDatabaseRepository
import com.example.testschedule.domain.use_case.account.study.certificate.GetCertificatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudyViewModel @Inject constructor(
    private val db: UserDatabaseRepository,
    private val getCertificatesUseCase: GetCertificatesUseCase
) : ViewModel() {

    val isLoading = mutableStateOf(true)

    val errorText = mutableStateOf("")
    val certificates = mutableListOf<CertificateModel>()


    init {
        getCertificates()
    }

    private fun getCertificates() {
        viewModelScope.launch {
            certificates.clear()
            certificates.addAll(db.getCertificates().sortedByDescending { it.id })
        }
        getCertificatesUseCase().onEach { res ->
            when(res){
                is Resource.Success -> {
                    isLoading.value = false
                    res.data?.let {
                        certificates.clear()
                        certificates.addAll(it.sortedByDescending { i -> i.id })
                        db.addCertificate(it)
                    }
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
                    errorText.value = ""
                    isLoading.value = true
                }
            }
        }.launchIn(viewModelScope)
    }
}