package com.example.testschedule.domain.repository

import com.example.testschedule.data.remote.dto.auth.UserBasicDataDto
import com.example.testschedule.domain.model.account.notifications.NotificationModel
import com.example.testschedule.domain.model.account.profile.AccountProfileModel
import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel
import com.example.testschedule.domain.model.schedule.ListOfGroupsModel
import com.example.testschedule.domain.model.schedule.ScheduleModel
import retrofit2.Call

interface IisAPIRepository {

    // Расписание
    suspend fun getListOfGroups(): List<ListOfGroupsModel>

    suspend fun getListOfEmployees(): List<ListOfEmployeesModel>

    suspend fun getSchedule(id: String): ScheduleModel

    suspend fun getCurrentWeek(): Int


    // Авторизация
    suspend fun loginToAccount(username: String, password: String) : Call<UserBasicDataDto?>


    // Работа с профилем

    // Получение данных об аккаунте
    suspend fun getAccountProfile(cookies: String) : AccountProfileModel

    // Получение уведомлений
    suspend fun getNotifications(cookies: String): List<NotificationModel>
}