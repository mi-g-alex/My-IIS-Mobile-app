package com.example.testschedule.data.repository

import com.example.testschedule.data.local.UserDao
import com.example.testschedule.data.local.entity.ScheduleEntity
import com.example.testschedule.domain.modal.schedule.ScheduleModel
import com.example.testschedule.domain.repository.UserDatabaseRepository
import javax.inject.Inject

class UserDatabaseRepositoryImpl @Inject constructor(
    private val dao: UserDao
) : UserDatabaseRepository {
    override suspend fun getSchedule(id: String): ScheduleModel = dao.getSchedule(id)

    override suspend fun setSchedule(it: ScheduleModel) {
        dao.setSchedule(
            ScheduleEntity(
            it.id,
            it.title,
            it.isGroupSchedule,
            it.startLessonsDate,
            it.endLessonsDate,
            it.endExamsDate,
            it.startExamsDate,
            it.employeeInfo,
            it.studentGroupInfo,
            it.schedules,
            it.exams
        )
        )
    }
}