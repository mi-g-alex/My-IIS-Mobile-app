package com.example.testschedule.presentation.schedule_screen.view_schedule_screen.spec_items

import com.example.testschedule.domain.model.schedule.ScheduleModel

data class LessonDay(
    val date: Long,
    val dayOfWeek: Int,
    val week: Int,
    val lessons: List<ScheduleModel.WeeksSchedule.Lesson>,
    val month: Int,
    val day: Int,
)

data class ExamDay(
    val date: Long,
    val exam: ScheduleModel.WeeksSchedule.Lesson,
    val month: Int,
    val day: Int,
)