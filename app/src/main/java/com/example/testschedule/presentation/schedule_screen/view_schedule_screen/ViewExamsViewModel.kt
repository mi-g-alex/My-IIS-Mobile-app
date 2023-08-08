package com.example.testschedule.presentation.schedule_screen.view_schedule_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testschedule.domain.model.schedule.ScheduleModel
import com.example.testschedule.domain.repository.UserDatabaseRepository
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.spec_items.ExamDay
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.spec_items.getExams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewExamsViewModel @Inject constructor(
    private val db: UserDatabaseRepository

) : ViewModel() {

    val exams = mutableStateOf<List<ExamDay>>(emptyList())
    val schedule = mutableStateOf<ScheduleModel?>(null)

    init {
        getScheduleFromDB()
    }

    fun getExams() {
        schedule.value?.let {
            if (it.startExamsDate != null && it.endExamsDate != null) {
                exams.value = it.exams?.let { it1 -> getExams(it1) } ?: emptyList()
            }
        }
    }

    private fun getScheduleFromDB(){
        viewModelScope.launch {
            schedule.value = db.getExams()
        }
    }

}