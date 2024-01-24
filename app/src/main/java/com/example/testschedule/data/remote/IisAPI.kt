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
import com.example.testschedule.data.remote.dto.account.study.certificate.CertificateItemDto
import com.example.testschedule.data.remote.dto.account.study.certificate.CreateCertificateDto
import com.example.testschedule.data.remote.dto.account.study.certificate.NewCertificatePlacesDto
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

    @GET("student-groups") /** Получение списка группы **/
    suspend fun getListOfGroups(): List<ListOfGroupsDto>

    @GET("employees/all") /** Получение списка преподавателей **/
    suspend fun getListOfEmployees(): List<ListOfEmployeesDto>

    @GET("schedule") /** Получение расписания группы **/
    suspend fun getGroupSchedule(@Query("studentGroup") groupNumber: String): ScheduleDto

    @GET("employees/schedule/{urlId}") /** Получение расписания преподавателя **/
    suspend fun getEmployeeSchedule(@Path("urlId") urlId: String): ScheduleDto

    @GET("schedule/current-week") /** Получение текущей недели **/
    suspend fun getCurrentWeek(): Int


    // Авторизация
    @POST("auth/login") /** Вход в аккаунте **/
    fun loginToAccount(@Body request: LoginAndPasswordDto): Call<UserBasicDataDto?>


    // Работа с профилем

    @GET("profiles/personal-cv") /** Получение данных об аккаунте **/
    suspend fun getAccountProfile(@Header("Cookie") cookies: String): AccountProfileDto

    // Уведомлений
    @GET("notifications") /** Получение списка всех уведомлений **/
    suspend fun getNotifications(
        @Header("Cookie") cookies: String,
        @Query("pageNumber") pagerNumber: Int,
        @Query("pageSize") pagerSize: Int
    ): NotificationsDto

    @PATCH("notifications") /** Пометить уведомления как прочитанные **/
    suspend fun readNotifications(
        @Header("Cookie") cookies: String,
        @Body data: List<ReadNotificationDto>
    )

    // Получение донных о заселение (общежитие + льготы)
    @GET("dormitory-queue-application") /** Получение списка заявок на общежитие **/
    suspend fun getDormitory(@Header("Cookie") cookies: String): List<DormitoryDto>

    @GET("dormitory-queue-application/privileges") /** Получение списка льгот **/
    suspend fun getPrivileges(@Header("Cookie") cookies: String): List<PrivilegesDto>

    // Группа
    @GET("student-groups/user-group-info") /** Получение списка группы **/
    suspend fun getGroupList(@Header("Cookie") cookies: String): GroupDto

    // Зачётки
    @GET("markbook") /** Получение список отметок из зачётки **/
    suspend fun getMarkBook(@Header("Cookie") cookies: String): MarkBookDto

    // Пропуски и взыскания
    @GET("omissions-by-student") /** Получение списка справок покрывающих пропуски **/
    suspend fun getOmissions(@Header("Cookie") cookies: String): OmissionsDto

    @GET("dormitory-queue-application/premium-penalty") /** Получение списка взысканий / поощрений **/
    suspend fun getPenalty(@Header("Cookie") cookies: String): List<PenaltyDto>

    //  События
    @GET("announcements") /** Получение списка события для аккаунта **/
    suspend fun getAnnouncements(@Header("Cookie") cookies: String): List<AnnouncementDto>

    // Рейтинг
    @GET("grade-book") /** Получение отметок в личном рейтинге **/
    suspend fun getRatingOfStudent(@Header("Cookie") cookies: String): List<RatingDto>

    // Учёба | Справки
    @GET("certificate") /** Получение списка справок, заканных для печати **/
    suspend fun getCertificates(@Header("Cookie") cookies: String): List<CertificateItemDto>

    @GET("certificate/places") /** Получение список, доступных для заказа **/
    suspend fun getNewCertificatePlaces(@Header("Cookie") cookies: String): List<NewCertificatePlacesDto>

    @POST("certificate/register") /** Заказать справку **/
    fun createCertificate(
        @Body request: CreateCertificateDto,
        @Header("Cookie") cookieValue: String
    ): Call<List<CertificateItemDto>>

    @GET("certificate/close") /** Отменить справку по id **/
    suspend fun closeCertificate(@Query("id") id: Int, @Header("Cookie") cookies: String): Any

}

