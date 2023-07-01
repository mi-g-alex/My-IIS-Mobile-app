package com.example.testschedule.data.remote

import com.example.testschedule.data.remote.dto.ScheduleDto
import com.example.testschedule.data.remote.dto.ScheduleEmployeeItemDto
import com.example.testschedule.data.remote.dto.ScheduleGroupItemDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IisAPI {

    @GET("student-groups") // Получение списка группы
    suspend fun getListOfGroups() : List<ScheduleGroupItemDto>

    @GET("employees/all") // Получение списка преподавателей
    suspend fun getListOfEmployees() : List<ScheduleEmployeeItemDto>

    @GET("schedule") // Получение расписания группы
    suspend fun getGroupSchedule(@Query("studentGroup") groupNumber : String) : ScheduleDto

    @GET("employees/schedule/{urlId}") // Получение расписания преподавателя
    suspend fun getEmployeeSchedule(@Path("urlId") urlId: String) : ScheduleDto
}