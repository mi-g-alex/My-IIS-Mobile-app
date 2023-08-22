package com.example.testschedule.data.remote

import com.example.testschedule.data.remote.dto.account.dormitory.DormitoryDto
import com.example.testschedule.data.remote.dto.account.dormitory.PrivilegesDto
import com.example.testschedule.data.remote.dto.account.group.GroupDto
import com.example.testschedule.data.remote.dto.account.mark_book.MarkBookDto
import com.example.testschedule.data.remote.dto.account.notifications.NotificationsDto
import com.example.testschedule.data.remote.dto.account.notifications.ReadNotificationDto
import com.example.testschedule.data.remote.dto.account.omissions.OmissionsDto
import com.example.testschedule.data.remote.dto.account.profile.AccountProfileDto
import com.example.testschedule.data.remote.dto.auth.LoginAndPasswordDto
import com.example.testschedule.data.remote.dto.auth.UserBasicDataDto
import com.example.testschedule.data.remote.dto.schedule_view.ListOfEmployeesDto
import com.example.testschedule.data.remote.dto.schedule_view.ListOfGroupsDto
import com.example.testschedule.data.remote.dto.schedule_view.ScheduleDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface IisAPI {

    // Расписание

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


    // Авторизация
    @POST("auth/login")
    fun loginToAccount(@Body request: LoginAndPasswordDto): Call<UserBasicDataDto?>


    // Работа с профилем

    // Получение данных об аккаунте
    @GET("profiles/personal-cv")
    suspend fun getAccountProfile(@Header("Cookie") cookies: String): AccountProfileDto

    // Получение уведомлений
    @GET("notifications")
    suspend fun getNotifications(
        @Header("Cookie") cookies: String,
        @Query("pageNumber") pagerNumber: Int,
        @Query("pageSize") pagerSize: Int
    ): NotificationsDto

    @PATCH("notifications")
    suspend fun readNotifications(
        @Header("Cookie") cookies: String,
        data: List<ReadNotificationDto>
    )

    // Получение донных о заселение (общежитие + льготы)
    @GET("dormitory-queue-application")
    suspend fun getDormitory(@Header("Cookie") cookies: String): List<DormitoryDto>

    @GET("dormitory-queue-application/privileges")
    suspend fun getPrivileges(@Header("Cookie") cookies: String): List<PrivilegesDto>

    // Получение списка групп
    @GET("student-groups/user-group-info")
    suspend fun getGroupList(@Header("Cookie") cookies: String): GroupDto

    // Получение зачётки
    @GET("markbook")
    suspend fun getMarkBook(@Header("Cookie") cookies: String): MarkBookDto

    // Получение списка справок и т.п. за пропуски
    @GET("omissions-by-student")
    suspend fun getOmissions(@Header("Cookie") cookies: String): List<OmissionsDto>
}

