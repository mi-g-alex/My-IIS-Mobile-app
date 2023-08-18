package com.example.testschedule.domain.repository

import com.example.testschedule.data.local.entity.schedule.ListOfSavedEntity
import com.example.testschedule.domain.model.account.profile.AccountProfileModel
import com.example.testschedule.domain.model.auth.LoginAndPasswordModel
import com.example.testschedule.domain.model.auth.UserBasicDataModel
import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel
import com.example.testschedule.domain.model.schedule.ListOfGroupsModel
import com.example.testschedule.domain.model.schedule.ScheduleModel

interface UserDatabaseRepository {

    // Schedule

    suspend fun getSchedule(id: String): ScheduleModel?

    suspend fun setSchedule(model: ScheduleModel)

    suspend fun deleteSchedule(id: String)

    suspend fun getAllSavedScheduleList(): List<ListOfSavedEntity>

    suspend fun addNewSavedScheduleToList(model: ListOfSavedEntity)

    suspend fun deleteFromSavedScheduleList(id: String)

    suspend fun insertAllGroupsList(groups: List<ListOfGroupsModel>)

    suspend fun getGroupById(name: String) : ListOfGroupsModel?

    suspend fun insertAllEmployeesList(employees: List<ListOfEmployeesModel>)

    suspend fun getEmployeeById(id: String) : ListOfEmployeesModel?

    suspend fun getAllGroupsList(): List<ListOfGroupsModel>

    suspend fun getAllEmployeesList(): List<ListOfEmployeesModel>

    suspend fun deleteAllGroupsList()

    suspend fun deleteAllEmployeesList()

    suspend fun setExams(model: ScheduleModel)

    suspend fun getExams()  : ScheduleModel


    // User Auth
    suspend fun setLoginAndPassword(data: LoginAndPasswordModel)

    suspend fun getLoginAndPassword() : LoginAndPasswordModel

    suspend fun deleteLoginAndPassword()

    suspend fun setUserBasicData(data: UserBasicDataModel)

    suspend fun getUserBasicData() : UserBasicDataModel?

    suspend fun deleteUserBasicData()


    // Cookie
    suspend fun getCookie() : String

    // Profile
    suspend fun setAccountProfile(data: AccountProfileModel)

    suspend fun getAccountProfile() : AccountProfileModel?

    suspend fun deleteAccountProfile()

}