package com.example.testschedule.data.local.entity.schedule

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testschedule.domain.model.schedule.ScheduleModel

@Entity
data class ScheduleEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val isGroupSchedule: Boolean,
    val startLessonsDate: Long?,
    val endLessonsDate: Long?,
    val startExamsDate: Long?,
    val endExamsDate: Long?,
    val employeeInfo: ScheduleModel.EmployeeInfo?,
    val studentGroupInfo: ScheduleModel.StudentGroupInfo?,
    val schedules: List<ScheduleModel.WeeksSchedule>,
    val exams: List<ScheduleModel.WeeksSchedule.Lesson>?
)