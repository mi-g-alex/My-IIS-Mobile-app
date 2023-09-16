package com.example.testschedule.data.repository

import com.example.testschedule.data.local.UserDao
import com.example.testschedule.data.local.entity.auth.LoginAndPasswordEntity
import com.example.testschedule.data.local.entity.auth.UserBasicDataEntity
import com.example.testschedule.data.local.entity.schedule.ListOfSavedEntity
import com.example.testschedule.data.local.entity.schedule.ScheduleEntity
import com.example.testschedule.domain.model.account.announcement.AnnouncementModel
import com.example.testschedule.domain.model.account.group.GroupModel
import com.example.testschedule.domain.model.account.mark_book.MarkBookModel
import com.example.testschedule.domain.model.account.notifications.NotificationModel
import com.example.testschedule.domain.model.account.omissions.OmissionsModel
import com.example.testschedule.domain.model.account.penalty.PenaltyModel
import com.example.testschedule.domain.model.account.profile.AccountProfileModel
import com.example.testschedule.domain.model.auth.LoginAndPasswordModel
import com.example.testschedule.domain.model.auth.UserBasicDataModel
import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel
import com.example.testschedule.domain.model.schedule.ListOfGroupsModel
import com.example.testschedule.domain.model.schedule.ScheduleModel
import com.example.testschedule.domain.repository.UserDatabaseRepository
import javax.inject.Inject

class UserDatabaseRepositoryImpl @Inject constructor(
    private val dao: UserDao
) : UserDatabaseRepository {
    override suspend fun getSchedule(id: String): ScheduleModel =
        dao.getSchedule(id = id)

    override suspend fun setSchedule(model: ScheduleModel) {
        dao.setSchedule(
            ScheduleEntity(
                id = model.id,
                title = model.title,
                isGroupSchedule = model.isGroupSchedule,
                startLessonsDate = model.startLessonsDate,
                endLessonsDate = model.endLessonsDate,
                startExamsDate = model.endExamsDate,
                endExamsDate = model.startExamsDate,
                employeeInfo = model.employeeInfo,
                studentGroupInfo = model.studentGroupInfo,
                schedules = model.schedules,
                exams = model.exams
            )
        )
    }

    override suspend fun deleteSchedule(id: String) {
        dao.deleteSchedule(id)
    }

    override suspend fun getAllSavedScheduleList(): List<ListOfSavedEntity> =
        dao.getAllSavedScheduleList()

    override suspend fun addNewSavedScheduleToList(model: ListOfSavedEntity) {
        dao.addNewSavedScheduleToList(model = model)
    }

    override suspend fun deleteFromSavedScheduleList(id: String) {
        dao.deleteFromSavedScheduleList(id = id)
    }

    override suspend fun insertAllGroupsList(groups: List<ListOfGroupsModel>) {
        dao.deleteAllGroupsList()
        groups.forEach {
            dao.insertAllGroupsList(group = it.toEntity())
        }
    }

    override suspend fun getGroupById(name: String): ListOfGroupsModel? =
        dao.getGroupById(name)?.toModel()

    override suspend fun insertAllEmployeesList(employees: List<ListOfEmployeesModel>) {
        dao.deleteAllEmployeesList()
        employees.forEach {
            dao.insertAllEmployeesList(it.toEntity())
        }
    }

    override suspend fun getEmployeeById(id: String): ListOfEmployeesModel? =
        dao.getEmployeeById(id = id)?.toModel()

    override suspend fun getAllGroupsList(): List<ListOfGroupsModel> {
        return dao.getAllGroupsList().map { it.toModel() }
    }

    override suspend fun getAllEmployeesList(): List<ListOfEmployeesModel> {
        return dao.getAllEmployeesList().map { it.toModel() }
    }

    override suspend fun deleteAllGroupsList() {
        dao.deleteAllGroupsList()
    }

    override suspend fun deleteAllEmployeesList() {
        dao.deleteAllEmployeesList()
    }

    override suspend fun setExams(model: ScheduleModel) {
        dao.setSchedule(
            ScheduleEntity(
                id = "EXAM",
                title = model.title,
                isGroupSchedule = model.isGroupSchedule,
                startLessonsDate = model.startLessonsDate,
                endLessonsDate = model.endLessonsDate,
                startExamsDate = model.endExamsDate,
                endExamsDate = model.startExamsDate,
                employeeInfo = model.employeeInfo,
                studentGroupInfo = model.studentGroupInfo,
                schedules = model.schedules,
                exams = model.exams
            )
        )
    }

    override suspend fun getExams(): ScheduleModel = dao.getSchedule("EXAM")


    // User Auth
    override suspend fun setLoginAndPassword(data: LoginAndPasswordModel) {
        val password = data.password
        var cof = 0
        data.username.forEach { cof += it.code % 8 }
        var newPass = ""
        password.forEachIndexed { i, c -> newPass += (c + cof + i).toString() }
        dao.setLoginAndPassword(
            LoginAndPasswordEntity(
                key = 0, username = data.username, password = newPass
            )
        )
    }

    override suspend fun getLoginAndPassword(): LoginAndPasswordModel {
        val data = dao.getLoginAndPassword()
        val password = data.password
        var cof = 0
        data.username.forEach { cof += it.code % 8 }
        var newPass = ""
        password.forEachIndexed { i, c -> newPass += (c - cof - i).toString() }
        return LoginAndPasswordModel(username = data.username, password = newPass)
    }

    override suspend fun deleteLoginAndPassword() {
        dao.deleteLoginAndPassword()
    }

    override suspend fun setUserBasicData(data: UserBasicDataModel) {
        dao.setUserBasicData(
            UserBasicDataEntity(
                key = 0,
                canStudentNote = data.canStudentNote,
                email = data.email,
                fio = data.fio,
                group = data.group,
                hasNotConfirmedContact = data.hasNotConfirmedContact,
                isGroupHead = data.isGroupHead,
                phone = data.phone,
                photoUrl = data.photoUrl,
                username = data.username,
                cookie = data.cookie
            )
        )
    }

    override suspend fun getUserBasicData(): UserBasicDataModel? {
        val data = dao.getUserBasicData() ?: return null

        return UserBasicDataModel(
            canStudentNote = data.canStudentNote,
            email = data.email,
            fio = data.fio,
            group = data.group,
            hasNotConfirmedContact = data.hasNotConfirmedContact,
            isGroupHead = data.isGroupHead,
            phone = data.phone,
            photoUrl = data.photoUrl,
            username = data.username,
            cookie = data.cookie
        )
    }

    override suspend fun deleteUserBasicData() {
        dao.deleteUserBasicData()
        dao.deleteAccountProfile()
        dao.deleteNotifications()
        dao.deleteUserGroup()
        dao.deleteMarkBook()
        dao.deleteOmissions()
        dao.deletePenalty()
    }


    // Cookie
    override suspend fun getCookie(): String = dao.getUserBasicData()?.cookie ?: ""

    // Profile
    override suspend fun setAccountProfile(data: AccountProfileModel) {
        dao.setAccountProfile(data.toEntity())
    }

    override suspend fun getAccountProfile(): AccountProfileModel? =
        dao.getAccountProfile()?.toModel()

    override suspend fun deleteAccountProfile() {
        dao.deleteAccountProfile()
    }

    // Notifications
    override suspend fun addNotifications(data: List<NotificationModel>) {
        dao.deleteNotifications()
        data.forEach {
            dao.addNotification(it.toEntity())
        }
    }

    override suspend fun updateNotificationStatus(id: List<Int>) {
        id.forEach { dao.updateNotificationStatus(it, true) }
    }

    override suspend fun getNotifications(): List<NotificationModel> =
        dao.getNotifications().map { it.toModel() }

    override suspend fun deleteNotifications() {
        dao.deleteNotifications()
    }

    // Group
    override suspend fun setUserGroup(data: GroupModel) {
        dao.setUserGroup(data.toEntity())
    }

    override suspend fun getUserGroup(): GroupModel? =
        dao.getUserGroup()?.toModel()

    override suspend fun deleteUserGroup() {
        dao.deleteUserGroup()
    }

    // Mark book
    override suspend fun setMarkBook(data: MarkBookModel) {
        dao.setMarkBook(data.toEntity())
    }

    override suspend fun getMarkBook(): MarkBookModel? =
        dao.getMarkBook()?.toModel()

    override suspend fun deleteMarkBook() {
        dao.deleteMarkBook()
    }

    // Omissions
    override suspend fun setOmissions(data: List<OmissionsModel>) {
        data.forEach { dao.setOmissions(it.toEntity()) }
    }

    override suspend fun getOmissions(): List<OmissionsModel> =
        dao.getOmissions()?.map { it.toModel() } ?: emptyList()

    override suspend fun deleteOmissions() {
        dao.deleteOmissions()
    }

    // Penalty
    override suspend fun setPenalty(data: List<PenaltyModel>) =
        data.forEach { it.toEntity() }

    override suspend fun getPenalty(): List<PenaltyModel> =
        dao.getPenalty()?.map { it.toModel() } ?: emptyList()

    override suspend fun deletePenalty() {
        dao.deletePenalty()
    }

    // Announcement
    override suspend fun addAnnouncements(data: List<AnnouncementModel>) {
        dao.deleteAnnouncements()
        data.forEach {
            dao.addAnnouncement(it.toEntity())
        }
    }
    override suspend fun getAnnouncements(): List<AnnouncementModel> =
        dao.getAnnouncements().map { it.toModel() }

    override suspend fun deleteAnnouncements() {
        dao.deleteAnnouncements()
    }
}