package com.example.testschedule.data.repository

import com.example.testschedule.data.local.UserDao
import com.example.testschedule.data.local.entity.ListOfSavedEntity
import com.example.testschedule.data.local.entity.ScheduleEntity
import com.example.testschedule.domain.model.schedule.ScheduleModel
import com.example.testschedule.domain.repository.UserDatabaseRepository
import javax.inject.Inject

class UserDatabaseRepositoryImpl @Inject constructor(
    private val dao: UserDao
) : UserDatabaseRepository {
    override suspend fun getSchedule(id: String): ScheduleModel =
        dao.getSchedule(id)

    override suspend fun setSchedule(model: ScheduleModel) {
        dao.setSchedule(
            ScheduleEntity(
                model.id,
                model.title,
                model.isGroupSchedule,
                model.startLessonsDate,
                model.endLessonsDate,
                model.endExamsDate,
                model.startExamsDate,
                model.employeeInfo,
                model.studentGroupInfo,
                model.schedules,
                model.exams
            )
        )
    }

    override suspend fun deleteSchedule(id: String) {
        dao.deleteSchedule(id)
    }

    override suspend fun getAllSavedScheduleList(): List<ListOfSavedEntity> =
        dao.getAllSavedScheduleList()

    override suspend fun addNewSavedScheduleToList(model: ListOfSavedEntity) {
        dao.addNewSavedScheduleToList(model)
    }

    override suspend fun deleteFromScheduleList(id: String) {
        dao.deleteSchedule(id)
    }
}