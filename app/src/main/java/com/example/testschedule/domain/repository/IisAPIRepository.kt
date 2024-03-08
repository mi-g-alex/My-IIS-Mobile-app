package com.example.testschedule.domain.repository

import com.example.testschedule.data.remote.dto.account.settings.email.SendConfirmMessageResponseDto
import com.example.testschedule.data.remote.dto.account.settings.password.ChangePasswordDto
import com.example.testschedule.data.remote.dto.account.study.mark_sheet.additional.MarkSheetTypeModel
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
import com.example.testschedule.domain.model.account.settings.email.ContactsModel
import com.example.testschedule.domain.model.account.settings.email.ContactsUpdateRequestModel
import com.example.testschedule.domain.model.account.settings.email.SendConfirmCodeRequestModel
import com.example.testschedule.domain.model.account.study.certificate.CertificateModel
import com.example.testschedule.domain.model.account.study.certificate.CreateCertificateModel
import com.example.testschedule.domain.model.account.study.certificate.NewCertificatePlacesModel
import com.example.testschedule.domain.model.account.study.mark_sheet.MarkSheetModel
import com.example.testschedule.domain.model.account.study.mark_sheet.create.CreateMarkSheetModel
import com.example.testschedule.domain.model.account.study.mark_sheet.create.MarkSheetSubjectsModel
import com.example.testschedule.domain.model.account.study.mark_sheet.create.SearchEmployeeMarkSheetModel
import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel
import com.example.testschedule.domain.model.schedule.ListOfGroupsModel
import com.example.testschedule.domain.model.schedule.ScheduleModel
import okhttp3.ResponseBody
import retrofit2.Call

interface IisAPIRepository {

    // Расписание
    suspend fun getListOfGroups(): List<ListOfGroupsModel>

    suspend fun getListOfEmployees(): List<ListOfEmployeesModel>

    suspend fun getSchedule(id: String): ScheduleModel

    suspend fun getCurrentWeek(): Int


    // Авторизация
    suspend fun loginToAccount(username: String, password: String): Call<UserBasicDataDto?>


    // Работа с профилем

    // Получение данных об аккаунте
    suspend fun getAccountProfile(cookies: String): AccountProfileModel

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

    suspend fun getNewCertificatePlaces(cookies: String): List<NewCertificatePlacesModel>

    suspend fun createCertificate(
        request: CreateCertificateModel,
        cookies: String
    ): Call<Any>

    suspend fun closeCertificate(id: Int, cookies: String)

    suspend fun getMarkSheets(cookies: String): List<MarkSheetModel>

    suspend fun closeMarkSheet(id: Int, cookies: String)

    suspend fun createMarkSheet(
        request: CreateMarkSheetModel,
        cookies: String
    ): Call<Any>

    suspend fun getMarkSheetTypes(cookies: String): List<MarkSheetTypeModel>

    suspend fun getMarkSheetSubjects(cookies: String): List<MarkSheetSubjectsModel>

    suspend fun searchEmployeeById(
        thId: Int?,
        focsId: Int?,
        cookies: String
    ): List<SearchEmployeeMarkSheetModel>

    suspend fun searchEmployeeByName(name: String): List<SearchEmployeeMarkSheetModel>


    // Settings

    suspend fun settingsUpdateBio(profile: AccountProfileModel, cookies: String)

    suspend fun settingsUpdateSkills(
        skills: List<AccountProfileModel.SkillModel>,
        cookies: String
    ): Call<ResponseBody?>

    suspend fun settingsUpdateLinks(
        links: List<AccountProfileModel.ReferenceModel>,
        cookies: String
    ): Any

    suspend fun settingsUpdateViewProfile(profile: AccountProfileModel, cookies: String)

    suspend fun settingsUpdateViewRating(profile: AccountProfileModel, cookies: String)

    suspend fun settingsUpdateViewJob(profile: AccountProfileModel, cookies: String)

    suspend fun settingsUpdatePassword(
        password: ChangePasswordDto,
        cookies: String
    ): Call<ResponseBody?>

    suspend fun settingsEmailGetContacts(cookies: String): ContactsModel

    suspend fun settingsEmailUpdate(
        mail: ContactsUpdateRequestModel,
        cookies: String
    ): Call<ResponseBody?>

    suspend fun settingsEmailGetConfirmCode(
        id: Int,
        cookies: String
    ): Call<SendConfirmMessageResponseDto>

    suspend fun settingsEmailConfirmMessage(
        code: SendConfirmCodeRequestModel,
        cookies: String
    ): Call<ResponseBody?>

}