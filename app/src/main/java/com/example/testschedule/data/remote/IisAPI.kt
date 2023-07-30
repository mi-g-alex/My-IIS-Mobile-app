package com.example.testschedule.data.remote

import com.example.testschedule.data.remote.dto.schedule_view.ScheduleDto
import com.example.testschedule.data.remote.dto.schedule_view.ListOfEmployeesDto
import com.example.testschedule.data.remote.dto.schedule_view.ListOfGroupsDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IisAPI {

    @GET("student-groups") // Получение списка группы
    suspend fun getListOfGroups(): List<ListOfGroupsDto>

    @GET("employees/all") // Получение списка преподавателей
    suspend fun getListOfEmployees(): List<ListOfEmployeesDto>

    @GET("schedule") // Получение расписания группы
    suspend fun getGroupSchedule(@Query("studentGroup") groupNumber: String): ScheduleDto

    @GET("employees/schedule/{urlId}") // Получение расписания преподавателя
    suspend fun getEmployeeSchedule(@Path("urlId") urlId: String): ScheduleDto

    @GET("schedule/current-week")
    suspend fun getCurrentWeek(): Int
}