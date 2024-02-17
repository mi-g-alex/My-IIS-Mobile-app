package com.example.testschedule.presentation.account.study_screen.create

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testschedule.common.Resource
import com.example.testschedule.data.remote.dto.account.study.mark_sheet.additional.MarkSheetTypeModel
import com.example.testschedule.domain.model.account.study.mark_sheet.create.MarkSheetSubjectsModel
import com.example.testschedule.domain.model.account.study.mark_sheet.create.SearchEmployeeMarkSheetModel
import com.example.testschedule.domain.use_case.account.study.mark_sheet.create.CreateMarkSheetUseCase
import com.example.testschedule.domain.use_case.account.study.mark_sheet.create.GetMarkSheetSubjectsUseCase
import com.example.testschedule.domain.use_case.account.study.mark_sheet.create.GetMarkSheetTypesUseCase
import com.example.testschedule.domain.use_case.account.study.mark_sheet.create.SearchEmployeeByIdUseCase
import com.example.testschedule.domain.use_case.account.study.mark_sheet.create.SearchEmployeeByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CreateMarkSheetViewModel @Inject constructor(
    private val getMarkSheetSubjectsUseCase: GetMarkSheetSubjectsUseCase,
    private val getMarkSheetTypesUseCase: GetMarkSheetTypesUseCase,
    private val searchEmployeeByIdUseCase: SearchEmployeeByIdUseCase,
    private val searchEmployeeByNameUseCase: SearchEmployeeByNameUseCase,
    private val createMarkSheetUseCase: CreateMarkSheetUseCase
) : ViewModel() {

    val isLoading = mutableStateOf(true)
    val errorText = mutableStateOf("")

    var subjectsList = listOf<MarkSheetSubjectsModel>()
    var typesList = listOf<MarkSheetTypeModel>()
    var specEmployees = listOf<SearchEmployeeMarkSheetModel>()
    var allEmployees = listOf<SearchEmployeeMarkSheetModel>()

    init {
        getSubjects()
        getTypes()
        getEmployeeByName()
    }

    val selectedType = mutableStateOf<MarkSheetTypeModel?>(null)
    val selectedSubject = mutableStateOf<MarkSheetSubjectsModel?>(null)
    val isGoodReason = mutableStateOf(false)
    val needAddDate = mutableStateOf(true)
    val hours = mutableDoubleStateOf(0.0)
    val selectedLesson = mutableStateOf<MarkSheetSubjectsModel.LessonTypeModel?>(null)
    val selectedEmployee = mutableStateOf<SearchEmployeeMarkSheetModel?>(null)

    private fun getSubjects() {
        getMarkSheetSubjectsUseCase().onEach { res ->
            when (res) {
                is Resource.Success -> {
                    if (typesList.isNotEmpty() && allEmployees.isNotEmpty()) isLoading.value = false
                    subjectsList = res.data ?: listOf()
                }

                is Resource.Loading -> {
                    isLoading.value = true
                }

                is Resource.Error -> {
                    if (typesList.isNotEmpty() && allEmployees.isNotEmpty()) isLoading.value = false
                    errorText.value = res.message.toString()
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getTypes() {
        getMarkSheetTypesUseCase().onEach { res ->
            when (res) {
                is Resource.Success -> {
                    if (subjectsList.isNotEmpty() && allEmployees.isNotEmpty()) isLoading.value =
                        false
                    typesList = res.data ?: listOf()
                }

                is Resource.Loading -> {
                    isLoading.value = true
                }

                is Resource.Error -> {
                    if (subjectsList.isNotEmpty() && allEmployees.isNotEmpty()) isLoading.value =
                        false
                    errorText.value = res.message.toString()
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getSpecEmployee() {
        searchEmployeeByIdUseCase(
            selectedLesson.value?.thId,
            selectedLesson.value?.focsId
        ).onEach { res ->
            when (res) {
                is Resource.Success -> {
                    if (subjectsList.isNotEmpty() && typesList.isNotEmpty()) isLoading.value = false
                    specEmployees = res.data ?: listOf()
                }

                is Resource.Loading -> {
                    isLoading.value = true
                }

                is Resource.Error -> {
                    if (subjectsList.isNotEmpty() && typesList.isNotEmpty()) isLoading.value = false
                    errorText.value = res.message.toString()
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getEmployeeByName() {
        searchEmployeeByNameUseCase("").onEach { res ->
            when (res) {
                is Resource.Success -> {
                    isLoading.value = false
                    allEmployees = res.data ?: listOf()
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


    fun createMarkSheet(date: String, onSuccess: () -> Unit, onError: () -> Unit) {
        if (selectedType.value != null && selectedEmployee.value != null)
            createMarkSheetUseCase(
                price = calcPrice(),
                markSheetType = selectedType.value!!,
                isGoodReason = isGoodReason.value,
                hours = if (selectedLesson.value?.isLab == true) hours.doubleValue.toInt()
                    .toString() else "",
                focsId = selectedLesson.value?.focsId,
                thId = selectedLesson.value?.thId,
                absentDate = if (needAddDate.value) date else "",
                employee = selectedEmployee.value!!
            ).onEach { res ->
                when (res) {
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

    fun calcPrice(): Double {
        return if (selectedEmployee.value == null) 0.0
        else Math.round(100.0 * (selectedEmployee.value?.price ?: 1.0) * hours.doubleValue) / 100.0
    }

}