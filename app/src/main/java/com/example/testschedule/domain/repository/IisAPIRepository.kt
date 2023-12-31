package com.example.testschedule.domain.repository

import com.example.testschedule.data.remote.dto.account.study.certificate.CertificateItemDto
import com.example.testschedule.data.remote.dto.auth.UserBasicDataDto
import com.example.testschedule.domain.model.account.announcement.AnnouncementModel
import com.example.testschedule.domain.model.account.dormitory.DormitoryModel
import com.example.testschedule.domain.model.account.dormitory.PrivilegesModel
import com.example.testschedule.domain.model.account.group.GroupModel
import com.example.testschedule.domain.model.account.mark_book.MarkBookModel
import com.example.testschedule.domain.model.account.notifications.NotificationModel
import com.example.testschedule.domain.model.account.omissions.OmissionsModel
import com.example.testschedule.domain.model.account.penalty.PenaltyModel
import com.example.testschedule.domain.model.account.profile.AccountProfileModel
import com.example.testschedule.domain.model.account.rating.RatingModel
import com.example.testschedule.domain.model.account.study.certificate.CertificateModel
import com.example.testschedule.domain.model.account.study.certificate.CreateCertificateModel
import com.example.testschedule.domain.model.account.study.certificate.NewCertificatePlacesModel
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

    // Уведомлений
    suspend fun getNotifications(cookies: String): List<NotificationModel>

    suspend fun readNotifications(cookies: String, data: List<Int>)

    // Общежитие и льготы
    suspend fun getDormitory(cookies: String): List<DormitoryModel>

    suspend fun getPrivileges(cookies: String): List<PrivilegesModel>

    // Группа
    suspend fun getUserGroup(cookies: String): GroupModel

    // Зачётка
    suspend fun getMarkBook(cookies: String): MarkBookModel

    // Пропуски
    suspend fun getOmissions(cookies: String): List<OmissionsModel>

    // Взыскания
    suspend fun getPenalty(cookies: String): List<PenaltyModel>

    // События
    suspend fun getAnnouncements(cookies: String): List<AnnouncementModel>

    // Рейтинг
    suspend fun getRating(cookies: String): RatingModel

    // Учёба
    suspend fun getCertificates(cookies: String): List<CertificateModel>

    suspend fun getNewCertificatePlaces(cookies: String) : List<NewCertificatePlacesModel>

    suspend fun createCertificate(request: CreateCertificateModel, cookies: String) : Call<List<CertificateItemDto>>

    suspend fun closeCertificate(id: Int, cookies: String): Any

}