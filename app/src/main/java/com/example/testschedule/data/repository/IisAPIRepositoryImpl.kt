package com.example.testschedule.data.repository

import com.example.testschedule.data.remote.IisAPI
import com.example.testschedule.data.remote.dto.account.notifications.NotificationsDto
import com.example.testschedule.data.remote.dto.account.notifications.ReadNotificationDto
import com.example.testschedule.data.remote.dto.auth.LoginAndPasswordDto
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

    // Уведомления
    override suspend fun getNotifications(cookies: String): List<NotificationModel> {
        val pageSize = 300
        var crnPage = 0
        var last: NotificationsDto
        val list: MutableList<NotificationModel> = mutableListOf()
        do {
            last =
                api.getNotifications(cookies = cookies, pagerNumber = crnPage, pagerSize = pageSize)
            last.notifications.forEach {
                list.add(it.toModel())
            }
            crnPage++
        } while (last.hasNext)
        return list.toList()
    }

    override suspend fun readNotifications(cookies: String, data: List<Int>) {
        val list = data.map { ReadNotificationDto(it, true) }
        api.readNotifications(cookies, list)
    }

    // Общага и льготы
    override suspend fun getDormitory(cookies: String): List<DormitoryModel> =
        api.getDormitory(cookies).map { it.toModel() }

    override suspend fun getPrivileges(cookies: String): List<PrivilegesModel> =
        api.getPrivileges(cookies).map { it.toModel() }

    // Группа
    override suspend fun getUserGroup(cookies: String): GroupModel =
        api.getGroupList(cookies).toModel()

    // Зачётка
    override suspend fun getMarkBook(cookies: String): MarkBookModel =
        api.getMarkBook(cookies).toModel()

    // Пропуски
    override suspend fun getOmissions(cookies: String): List<OmissionsModel> =
        api.getOmissions(cookies).omissionDtoList?.map { it.toModel() } ?: emptyList()

    // Взыскания
    override suspend fun getPenalty(cookies: String): List<PenaltyModel> =
        api.getPenalty(cookies).map { it.toModel() }

    // События
    override suspend fun getAnnouncements(cookies: String): List<AnnouncementModel> =
        api.getAnnouncements(cookies).map { it.toModel() }

    // Рейтинг
    override suspend fun getRating(cookies: String): RatingModel =
        api.getRatingOfStudent(cookies)[0].toModel()
}