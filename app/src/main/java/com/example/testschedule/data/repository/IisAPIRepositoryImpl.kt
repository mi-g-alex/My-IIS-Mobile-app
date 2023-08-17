package com.example.testschedule.data.repository

import com.example.testschedule.data.remote.IisAPI
import com.example.testschedule.data.remote.dto.auth.LoginAndPasswordDto
import com.example.testschedule.data.remote.dto.auth.UserBasicDataDto
import com.example.testschedule.domain.model.account.profile.AccountProfileModel
import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel
import com.example.testschedule.domain.model.schedule.ListOfGroupsModel
import com.example.testschedule.domain.model.schedule.ScheduleModel
import com.example.testschedule.domain.model.schedule.scheduleFromDtoToModel
import com.example.testschedule.domain.repository.IisAPIRepository
import retrofit2.Call
import javax.inject.Inject

class IisAPIRepositoryImpl @Inject constructor(
    private val api: IisAPI
) : IisAPIRepository {

    // Расписание
    override suspend fun getListOfGroups(): List<ListOfGroupsModel> =
        api.getListOfGroups().map { it.toModel() }


    override suspend fun getListOfEmployees(): List<ListOfEmployeesModel> =
        api.getListOfEmployees().map { it.toModel() }


    override suspend fun getSchedule(id: String): ScheduleModel =
        if (id[0] in '0'..'9') {
            scheduleFromDtoToModel(api.getGroupSchedule(id))
            //scheduleFromDtoToModel(Constants.test)
        } else {
            scheduleFromDtoToModel(api.getEmployeeSchedule(id))
        }


    override suspend fun getCurrentWeek(): Int = api.getCurrentWeek()


    // Авторизация
    override suspend fun loginToAccount(
        username: String,
        password: String
    ): Call<UserBasicDataDto?> =
        api.loginToAccount(LoginAndPasswordDto(username, password))


    // Профиль
    override suspend fun getAccountProfile(cookies: String): AccountProfileModel =
        api.getAccountProfile(cookies).toModel()

}