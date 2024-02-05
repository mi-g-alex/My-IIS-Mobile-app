package com.example.testschedule.presentation.account.study_screen.create

import androidx.compose.runtime.clearCompositionErrors
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testschedule.common.Resource
import com.example.testschedule.domain.model.account.study.certificate.NewCertificatePlacesModel
import com.example.testschedule.domain.repository.UserDatabaseRepository
import com.example.testschedule.domain.use_case.account.study.certificate.CreateCertificatesUseCase
import com.example.testschedule.domain.use_case.account.study.certificate.GetCertificatesNewPlacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCertificateViewModel @Inject constructor(
    private val getNewCertificatesList: GetCertificatesNewPlacesUseCase,
    private val createCertificatesUseCase: CreateCertificatesUseCase,
    private val db: UserDatabaseRepository
) : ViewModel() {

    var listOfPlaces: List<NewCertificatePlacesModel> = listOf()
    val isLoading = mutableStateOf(true)
    val errorText = mutableStateOf("")

    init {
        getPlaces()
    }

    var selectedPlace = mutableStateOf("")
    var noteText = mutableStateOf("")
    var selectedPlaceType = mutableIntStateOf(0)
    var isHerbOption = mutableStateOf(false)
    var countOfCertificates = mutableIntStateOf(1)


    private fun getPlaces() {
        viewModelScope.launch {
            listOfPlaces = db.getCertificatesPlaces()
        }
        getNewCertificatesList().onEach { res ->
            when (res) {
                is Resource.Success -> {
                    isLoading.value = false
                    listOfPlaces = res.data ?: listOf()
                }

                is Resource.Loading -> {
                    isLoading.value = true
                }

                is Resource.Error -> {
                    isLoading.value = false
                    errorText.value = res.message.toString()
                }
            }
        }.launchIn(viewModelScope)
    }

    fun createCertificate(onSuccess: () -> Unit, onError: () -> Unit) {
        val place =
            selectedPlace.value + (if (noteText.value.isNotEmpty() && selectedPlaceType.intValue !in (1..2)) " (" + noteText.value + ")" else "")
        val type = if(isHerbOption.value) "гербовая" else "обычная"
        val count = countOfCertificates.intValue
        createCertificatesUseCase.invoke(place, type, count).onEach { res ->
            when(res) {
                is Resource.Success -> {
                    onSuccess()
                }
                is Resource.Loading -> {
                    isLoading.value = true
                }
                is Resource.Error -> {
                    isLoading.value = false
                    errorText.value = res.message.toString()
                    onError()
                }
            }
        }.launchIn(viewModelScope)
    }

}