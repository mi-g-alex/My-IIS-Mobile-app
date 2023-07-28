package com.example.testschedule.presentation.schedule_screen.view_schedule_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testschedule.common.Resource
import com.example.testschedule.data.local.entity.ListOfSavedEntity
import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel
import com.example.testschedule.domain.model.schedule.ListOfGroupsModel
import com.example.testschedule.domain.repository.UserDatabaseRepository
import com.example.testschedule.domain.use_case.schedule.get_schedule.GetScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewScheduleViewModel @Inject constructor(
    private val getScheduleUseCase: GetScheduleUseCase,
    private val db: UserDatabaseRepository
) : ViewModel() {

    private val _state = mutableStateOf(ViewScheduleState())
    val state: State<ViewScheduleState> = _state

    var title: MutableState<String> = mutableStateOf("")
    var savedSchedule: MutableState<List<ListOfSavedEntity>?> = mutableStateOf(listOf())
    var savedGroups: MutableState<List<ListOfGroupsModel>> = mutableStateOf(listOf())
    var savedEmployees: MutableState<List<ListOfEmployeesModel>> = mutableStateOf(listOf())

    init {
        getSaved()
    }

    fun getSchedule(id: String) {
        getScheduleUseCase(id = id).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = ViewScheduleState(schedule = result.data)
                    /*result.data?.let {
                        db.setSchedule(it)
                    }*/
                }

                is Resource.Loading -> {
                    val a = db.getSchedule(id)
                    _state.value = ViewScheduleState(isLoading = true, schedule = a)
                }

                is Resource.Error -> {
                    _state.value = ViewScheduleState(error = result.message)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getSaved() {
        viewModelScope.launch {
            savedSchedule.value = db.getAllSavedScheduleList()
            val tmpGroups: MutableList<ListOfGroupsModel> = mutableListOf()
            val tmpEmployees: MutableList<ListOfEmployeesModel> = mutableListOf()
            savedSchedule.value!!.forEach {
                if(it.isGroup) {
                    db.getGroupById(it.id)?.let { it1 -> tmpGroups.add(it1) }
                } else {
                    db.getEmployeeById(it.id)?.let { it1 -> tmpEmployees.add(it1) }
                }
            }
            savedGroups.value = tmpGroups.toList()
            savedEmployees.value = tmpEmployees.toList()
        }
    }

}