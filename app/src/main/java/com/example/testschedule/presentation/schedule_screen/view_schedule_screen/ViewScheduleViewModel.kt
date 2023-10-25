package com.example.testschedule.presentation.schedule_screen.view_schedule_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testschedule.common.Resource
import com.example.testschedule.data.local.entity.schedule.ListOfSavedEntity
import com.example.testschedule.di.AppModule
import com.example.testschedule.domain.model.auth.UserBasicDataModel
import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel
import com.example.testschedule.domain.model.schedule.ListOfGroupsModel
import com.example.testschedule.domain.repository.UserDatabaseRepository
import com.example.testschedule.domain.use_case.schedule.get_schedule.GetCurrentWeekUseCase
import com.example.testschedule.domain.use_case.schedule.get_schedule.GetScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.GregorianCalendar
import javax.inject.Inject

@HiltViewModel
class ViewScheduleViewModel @Inject constructor(
    private val myPreference: AppModule.MyPreference,
    private val getScheduleUseCase: GetScheduleUseCase,
    private val getCurrentWeek: GetCurrentWeekUseCase,
    private val db: UserDatabaseRepository

) : ViewModel() {

    private val _state = mutableStateOf(ViewScheduleState())
    val state: State<ViewScheduleState> = _state

    var title: MutableState<String> = mutableStateOf("")
    private var lastSelectedSchedule by mutableStateOf("")
    var savedSchedule: MutableState<List<ListOfSavedEntity>?> = mutableStateOf(listOf())
    var savedGroups: MutableState<List<ListOfGroupsModel>> = mutableStateOf(listOf())
    var savedEmployees: MutableState<List<ListOfEmployeesModel>> = mutableStateOf(listOf())
    var userData: MutableState<UserBasicDataModel?> = mutableStateOf(null)

    init {
        getSaved()
        getCurrentWeek()
    }

    fun getSchedule(id: String) {
        lastSelectedSchedule = id
        getScheduleUseCase(id = id).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    if (result.data?.id == lastSelectedSchedule) {
                        _state.value = ViewScheduleState(schedule = result.data)
                        title.value = result.data.title
                    }
                    result.data?.let {
                        db.deleteSchedule(id)
                        if (savedSchedule.value?.any { f -> f.id == it.id } == true)
                            db.setSchedule(it)
                    }
                }

                is Resource.Loading -> {
                    val a = db.getSchedule(id)
                    _state.value = ViewScheduleState(isLoading = true, schedule = a)
                }

                is Resource.Error -> {
                    val a = db.getSchedule(id)
                    _state.value = ViewScheduleState(error = result.message, schedule = a)
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
                if (it.isGroup) {
                    db.getGroupById(it.id)?.let { it1 -> tmpGroups.add(it1) }
                } else {
                    db.getEmployeeById(it.id)?.let { it1 -> tmpEmployees.add(it1) }
                }
            }
            savedGroups.value = tmpGroups.toList()
            savedEmployees.value = tmpEmployees.toList()
        }
    }

    private fun getCurrentWeek() {
        getCurrentWeek.invoke().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    val week = result.data
                    if (week != null) {
                        val calTmp = Calendar.getInstance()
                        calTmp.timeInMillis += 10800000 - calTmp.timeZone.rawOffset
                        val cal = GregorianCalendar(
                            calTmp.get(Calendar.YEAR),
                            calTmp.get(Calendar.MONTH),
                            calTmp.get(Calendar.DAY_OF_MONTH),
                            0, 0, 0
                        )
                        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                            cal.timeInMillis -= 6 * 24 * 60 * 60 * 1000
                        } else {
                            cal.timeInMillis -= (cal.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY) * 24 * 60 * 60 * 1000
                        }
                        myPreference.setCurrentWeek(cal.timeInMillis, week)
                    }
                }

                is Resource.Loading -> {

                }

                is Resource.Error -> {

                }
            }
        }.launchIn(viewModelScope)
    }

    fun setExamsToDB() {
        viewModelScope.launch { state.value.schedule?.let { db.setExams(it) } }
    }

    fun getProfile() {
        viewModelScope.launch {
            userData.value = db.getUserBasicData()
        }
    }
}