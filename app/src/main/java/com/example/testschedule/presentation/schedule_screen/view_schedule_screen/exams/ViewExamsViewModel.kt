package com.example.testschedule.presentation.schedule_screen.view_schedule_screen.exams

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.testschedule.domain.model.schedule.ScheduleModel
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.spec_items.ExamDay
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.spec_items.getExams
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewExamsViewModel @Inject constructor() : ViewModel() {

    val exams = mutableStateOf<List<ExamDay>>(emptyList())
    val schedule = mutableStateOf<ScheduleModel?>(null)

    fun getExams() {
        schedule.value?.let {
            if (it.startExamsDate != null && it.endExamsDate != null) {
                exams.value = it.exams?.let { it1 ->
                    getExams(
                        it1.sortedWith(
                            compareBy({ it.dateLesson },
                                { it.startLessonTime })
                        )
                    )
                } ?: emptyList()
            }
        }
    }
}