package com.example.testschedule.presentation.schedule_screen.add_schedule_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testschedule.common.Resource
import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel
import com.example.testschedule.domain.model.schedule.ListOfGroupsModel
import com.example.testschedule.domain.repository.UserDatabaseRepository
import com.example.testschedule.domain.use_case.schedule.get_schedule.GetListOfEmployeesUseCase
import com.example.testschedule.domain.use_case.schedule.get_schedule.GetListOfGroupsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddScheduleViewModel @Inject constructor(
    private val getListOfGroupsUseCase: GetListOfGroupsUseCase,
    private val getListOfEmployeesUseCase: GetListOfEmployeesUseCase,
    private val db: UserDatabaseRepository
) : ViewModel() {

    private val _state = mutableStateOf(AddScheduleState())
    val state: State<AddScheduleState> = _state

    var groups: List<ListOfGroupsModel>? = listOf()
    var employees: List<ListOfEmployeesModel>? = listOf()

    init {
        getLists()
        viewModelScope.launch {
            groups = db.getAllGroupsList()
            employees = db.getAllEmployeesList()
        }
    }

    private fun getLists() {
        var groupsTmp: List<ListOfGroupsModel>? = listOf()
        var employeesTmp: List<ListOfEmployeesModel>? = listOf()
        getListOfGroupsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    groupsTmp = result.data?.sortedBy { it.course }?.filter { it.course > 0}
                    viewModelScope.launch { groupsTmp?.let { db.insertAllGroupsList(it) } }
                    if (employeesTmp?.isNotEmpty() == true) {
                        groups = groupsTmp
                        employees = employeesTmp
                        _state.value = AddScheduleState()
                    }
                }

                is Resource.Loading -> {
                    _state.value = AddScheduleState(isLoading = true)
                }

                is Resource.Error -> {
                    _state.value = AddScheduleState(error = result.message)
                }
            }
        }.launchIn(viewModelScope)

        getListOfEmployeesUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    employeesTmp = result.data
                    viewModelScope.launch { employeesTmp?.let { db.insertAllEmployeesList(it) } }
                    if (groupsTmp?.isNotEmpty() == true) {
                        groups = groupsTmp
                        employees = employeesTmp
                        _state.value = AddScheduleState()
                    }
                }

                is Resource.Loading -> {
                    _state.value = AddScheduleState(isLoading = true)
                }

                is Resource.Error -> {
                    _state.value = AddScheduleState(error = result.message)
                }
            }
        }.launchIn(viewModelScope)
    }

}