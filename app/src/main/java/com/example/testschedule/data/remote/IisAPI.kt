package com.example.testschedule.data.remote

import com.example.testschedule.data.remote.dto.account.announcement.AnnouncementDto
import com.example.testschedule.data.remote.dto.account.dormitory.DormitoryDto
import com.example.testschedule.data.remote.dto.account.dormitory.PrivilegesDto
import com.example.testschedule.data.remote.dto.account.group.GroupDto
import com.example.testschedule.data.remote.dto.account.mark_book.MarkBookDto
import com.example.testschedule.data.remote.dto.account.notifications.NotificationsDto
import com.example.testschedule.data.remote.dto.account.notifications.ReadNotificationDto
import com.example.testschedule.data.remote.dto.account.omissions.OmissionsDto
import com.example.testschedule.data.remote.dto.account.penalty.PenaltyDto
import com.example.testschedule.data.remote.dto.account.profile.AccountProfileDto
import com.example.testschedule.data.remote.dto.account.rating.RatingDto
import com.example.testschedule.data.remote.dto.account.settings.email.ContactsDto
import com.example.testschedule.data.remote.dto.account.settings.email.ContactsUpdateRequestDto
import com.example.testschedule.data.remote.dto.account.settings.email.SendConfirmCodeRequestDto
import com.example.testschedule.data.remote.dto.account.settings.email.SendConfirmMessageResponseDto
import com.example.testschedule.data.remote.dto.account.settings.password.ChangePasswordDto
import com.example.testschedule.data.remote.dto.account.study.certificate.CertificateItemDto
import com.example.testschedule.data.remote.dto.account.study.certificate.CreateCertificateDto
import com.example.testschedule.data.remote.dto.account.study.certificate.NewCertificatePlacesDto
import com.example.testschedule.data.remote.dto.account.study.mark_sheet.MarkSheetItemDto
import com.example.testschedule.data.remote.dto.account.study.mark_sheet.additional.MarkSheetTypeDto
import com.example.testschedule.data.remote.dto.account.study.mark_sheet.create.CreateMarkSheetDto
import com.example.testschedule.data.remote.dto.account.study.mark_sheet.create.SearchEmployeeMarkSheetDto
import com.example.testschedule.data.remote.dto.account.study.mark_sheet.create.SubjectsMarkSheetItemDto
import com.example.testschedule.data.remote.dto.auth.LoginAndPasswordDto
import com.example.testschedule.data.remote.dto.auth.UserBasicDataDto
import com.example.testschedule.data.remote.dto.schedule_view.ListOfEmployeesDto
import com.example.testschedule.data.remote.dto.schedule_view.ListOfGroupsDto
import com.example.testschedule.data.remote.dto.schedule_view.ScheduleDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface IisAPI {

    // Расписание

    @GET("student-groups")
    /** Получение списка группы **/
    suspend fun getListOfGroups(): List<ListOfGroupsDto>

    @GET("employees/all")
    /** Получение списка преподавателей **/
    suspend fun getListOfEmployees(): List<ListOfEmployeesDto>

    @GET("schedule")
    /** Получение расписания группы **/
    suspend fun getGroupSchedule(@Query("studentGroup") groupNumber: String): ScheduleDto

    @GET("employees/schedule/{urlId}")
    /** Получение расписания преподавателя **/
    suspend fun getEmployeeSchedule(@Path("urlId") urlId: String): ScheduleDto

    @GET("schedule/current-week")
    /** Получение текущей недели **/
    suspend fun getCurrentWeek(): Int


    // Авторизация
    @POST("auth/login")
            /** Вход в аккаунте **/
    fun loginToAccount(@Body request: LoginAndPasswordDto): Call<UserBasicDataDto?>


    // Работа с профилем

    @GET("profiles/personal-cv")
    /** Получение данных об аккаунте **/
    suspend fun getAccountProfile(@Header("Cookie") cookies: String): AccountProfileDto

    // Уведомлений
    @GET("notifications")
    /** Получение списка всех уведомлений **/
    suspend fun getNotifications(
        @Header("Cookie") cookies: String,
        @Query("pageNumber") pagerNumber: Int,
        @Query("pageSize") pagerSize: Int
    ): NotificationsDto

    @PATCH("notifications")
    /** Пометить уведомления как прочитанные **/
    suspend fun readNotifications(
        @Header("Cookie") cookies: String,
        @Body data: List<ReadNotificationDto>
    )

    // Получение донных о заселение (общежитие + льготы)
    @GET("dormitory-queue-application")
    /** Получение списка заявок на общежитие **/
    suspend fun getDormitory(@Header("Cookie") cookies: String): List<DormitoryDto>

    @GET("dormitory-queue-application/privileges")
    /** Получение списка льгот **/
    suspend fun getPrivileges(@Header("Cookie") cookies: String): List<PrivilegesDto>

    // Группа
    @GET("student-groups/user-group-info")
    /** Получение списка группы **/
    suspend fun getGroupList(@Header("Cookie") cookies: String): GroupDto

    // Зачётки
    @GET("markbook")
    /** Получение список отметок из зачётки **/
    suspend fun getMarkBook(@Header("Cookie") cookies: String): MarkBookDto

    // Пропуски и взыскания
    @GET("omissions-by-student")
    /** Получение списка справок покрывающих пропуски **/
    suspend fun getOmissions(@Header("Cookie") cookies: String): OmissionsDto

    @GET("dormitory-queue-application/premium-penalty")
    /** Получение списка взысканий / поощрений **/
    suspend fun getPenalty(@Header("Cookie") cookies: String): List<PenaltyDto>

    //  События
    @GET("announcements")
    /** Получение списка события для аккаунта **/
    suspend fun getAnnouncements(@Header("Cookie") cookies: String): List<AnnouncementDto>

    // Рейтинг
    @GET("grade-book")
    /** Получение отметок в личном рейтинге **/
    suspend fun getRatingOfStudent(@Header("Cookie") cookies: String): List<RatingDto>

    // Учёба | Справки | Ведомостички
    @GET("certificate")
    /** Получение списка справок, заканных для печати **/
    suspend fun getCertificates(@Header("Cookie") cookies: String): List<CertificateItemDto>

    @GET("certificate/places")
    /** Получение список, доступных для заказа **/
    suspend fun getNewCertificatePlaces(@Header("Cookie") cookieValue: String): List<NewCertificatePlacesDto>

    @POST("certificate/register")
            /** Заказать справку **/
    fun createCertificate(
        @Body request: CreateCertificateDto,
        @Header("Cookie") cookieValue: String
    ): Call<Any>

    @GET("certificate/close")
    /** Отменить справку по id **/
    suspend fun closeCertificate(@Query("id") id: Int, @Header("Cookie") cookies: String)

    @GET("mark-sheet")
    /** Получение списка ведомостичек **/
    suspend fun getMarkSheets(@Header("Cookie") cookies: String): List<MarkSheetItemDto>

    @GET("mark-sheet/close")
    /** Отменить ведомостичку по id **/
    suspend fun closeMarkSheet(@Query("id") id: Int, @Header("Cookie") cookies: String)

    @POST("mark-sheet")
            /** Заказать ведомость **/
    fun createMarkSheet(
        @Body request: CreateMarkSheetDto,
        @Header("Cookie") cookieValue: String
    ): Call<Any>

    @GET("mark-sheet/types")
    /** Получение списка типов справок **/
    suspend fun getMarkSheetTypes(@Header("Cookie") cookies: String): List<MarkSheetTypeDto>

    @GET("mark-sheet/subjects")
    /** Получение списка предметов справок **/
    suspend fun getMarkSheetSubjects(@Header("Cookie") cookies: String): List<SubjectsMarkSheetItemDto>

    @GET("employees/mark-sheet")
    /** Получение списка преподавателя по thId **/
    suspend fun searchEmployeeThIdMarkSheet(
        @Query("thId") id: Int,
        @Header("Cookie") cookies: String
    ): List<SearchEmployeeMarkSheetDto>?

    @GET("employees/mark-sheet")
    /** Получение списка преподавателя по focsId **/
    suspend fun searchEmployeeFocsIdMarkSheet(
        @Query("focsId") id: Int,
        @Header("Cookie") cookies: String
    ): List<SearchEmployeeMarkSheetDto>?

    @GET("employees/fio/requests")
    /** Поиск преподавателя по фио **/
    suspend fun searchEmployeeNameMarkSheet(
        @Query("employee-fio") id: String
    ): List<SearchEmployeeMarkSheetDto>?

    // Настройки
    @PUT("profiles/summary")
    /** Обновление "О себе"**/
    suspend fun settingsUpdateBio(
        @Body profile: AccountProfileDto,
        @Header("Cookie") cookies: String
    )

    @POST("profiles/my-skills")
            /** Обновление навыков **/
    fun settingsUpdateSkills(
        @Body skills: List<AccountProfileDto.SkillDto>,
        @Header("Cookie") cookies: String
    ): Call<ResponseBody?>

    @PUT("profiles/my-references")
    /** Обновление ссылок**/
    suspend fun settingsUpdateLinks(
        @Body skills: List<AccountProfileDto.ReferenceDto>,
        @Header("Cookie") cookies: String
    )

    @PUT("profiles/personal-cv-published")
    /** Обновление настройки профиль **/
    suspend fun settingsUpdateViewProfile(
        @Body profile: AccountProfileDto,
        @Header("Cookie") cookies: String
    )

    @PUT("profiles/personal-cv-rating")
    /** Обновление настройки рейтинг **/
    suspend fun settingsUpdateViewRating(
        @Body profile: AccountProfileDto,
        @Header("Cookie") cookies: String
    )

    @PUT("profiles/personal-cv-searching-job")
    /** Обновление настройки работа **/
    suspend fun settingsUpdateViewJob(
        @Body profile: AccountProfileDto,
        @Header("Cookie") cookies: String
    )

    @POST("settings/password/change")
            /** Смена пароля аккаунта **/
    fun settingsChangePassword(
        @Body password: ChangePasswordDto,
        @Header("Cookie") cookieValue: String
    ): Call<ResponseBody?>

    // Email
    @GET("settings/contacts")
    /** Получение списка из 1 mail и его ID **/
    suspend fun settingsEmailGetContacts(
        @Header("Cookie") cookies: String
    ): ContactsDto

    @POST("settings/contact/update")
    /** Отправвка нового Email **/
    fun settingsEmailUpdate(
        @Body mail: ContactsUpdateRequestDto,
        @Header("Cookie") cookies: String
    ): Call<ResponseBody?>

    @POST("settings/contact/send-confirm-message")
    /** Отправка кода на почту **/
    fun settingsEmailGetConfirmCode(
        @Body id: Int,
        @Header("Cookie") cookies: String
    ): Call<SendConfirmMessageResponseDto>

    @POST("settings/contact/confirm")
    /** Отправка кода для проверки **/
    fun settingsEmailConfirmMessage(
        @Body code: SendConfirmCodeRequestDto,
        @Header("Cookie") cookies: String
    ): Call<ResponseBody?>

}


