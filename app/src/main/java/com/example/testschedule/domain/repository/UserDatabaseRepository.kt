package com.example.testschedule.domain.repository

import com.example.testschedule.data.local.entity.schedule.ListOfSavedEntity
import com.example.testschedule.domain.model.account.announcement.AnnouncementModel
import com.example.testschedule.domain.model.account.group.GroupModel
import com.example.testschedule.domain.model.account.mark_book.MarkBookModel
import com.example.testschedule.domain.model.account.notifications.NotificationModel
import com.example.testschedule.domain.model.account.omissions.OmissionsModel
import com.example.testschedule.domain.model.account.penalty.PenaltyModel
import com.example.testschedule.domain.model.account.profile.AccountProfileModel
import com.example.testschedule.domain.model.account.study.certificate.CertificateModel
import com.example.testschedule.domain.model.account.study.certificate.NewCertificatePlacesModel
import com.example.testschedule.domain.model.account.study.mark_sheet.MarkSheetModel
import com.example.testschedule.domain.model.auth.LoginAndPasswordModel
import com.example.testschedule.domain.model.auth.UserBasicDataModel
import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel
import com.example.testschedule.domain.model.schedule.ListOfGroupsModel
import com.example.testschedule.domain.model.schedule.ScheduleModel

interface UserDatabaseRepository {

    // Schedule

    suspend fun getSchedule(id: String): ScheduleModel?

    suspend fun setSchedule(model: ScheduleModel)

    suspend fun deleteSchedule(id: String)

    suspend fun getAllSavedScheduleList(): List<ListOfSavedEntity>

    suspend fun addNewSavedScheduleToList(model: ListOfSavedEntity)

    suspend fun deleteFromSavedScheduleList(id: String)

    suspend fun insertAllGroupsList(groups: List<ListOfGroupsModel>)

    suspend fun getGroupById(name: String): ListOfGroupsModel?

    suspend fun insertAllEmployeesList(employees: List<ListOfEmployeesModel>)

    suspend fun getEmployeeById(id: String): ListOfEmployeesModel?

    suspend fun getAllGroupsList(): List<ListOfGroupsModel>

    suspend fun getAllEmployeesList(): List<ListOfEmployeesModel>

    suspend fun deleteAllGroupsList()

    suspend fun deleteAllEmployeesList()

    suspend fun setExams(model: ScheduleModel)

    suspend fun getExams(): ScheduleModel


    // User Auth
    suspend fun setLoginAndPassword(data: LoginAndPasswordModel)

    suspend fun getLoginAndPassword(): LoginAndPasswordModel

    suspend fun deleteLoginAndPassword()

    suspend fun setUserBasicData(data: UserBasicDataModel)

    suspend fun getUserBasicData(): UserBasicDataModel?

    suspend fun deleteUserBasicData()


    // Cookie
    suspend fun getCookie(): String

    // Profile
    suspend fun setAccountProfile(data: AccountProfileModel)

    suspend fun getAccountProfile(): AccountProfileModel?

    suspend fun deleteAccountProfile()

    // Notification
    suspend fun addNotifications(data: List<NotificationModel>)

    suspend fun updateNotificationStatus(id: List<Int>)

    suspend fun getNotifications(): List<NotificationModel>

    suspend fun deleteNotifications()

    // Group

    suspend fun setUserGroup(data: GroupModel)

    suspend fun getUserGroup(): GroupModel?

    suspend fun deleteUserGroup()

    // Mark book
    suspend fun setMarkBook(data: MarkBookModel)

    suspend fun getMarkBook(): MarkBookModel?

    suspend fun deleteMarkBook()

    // Omissions
    suspend fun addOmissions(data: List<OmissionsModel>)

    suspend fun getOmissions(): List<OmissionsModel>

    suspend fun deleteOmissions()

    // Penalty
    suspend fun addPenalty(data: List<PenaltyModel>)

    suspend fun getPenalty(): List<PenaltyModel>

    suspend fun deletePenalty()

    // Announcement
    suspend fun addAnnouncements(data: List<AnnouncementModel>)

    suspend fun getAnnouncements(): List<AnnouncementModel>

    suspend fun deleteAnnouncements()

    // Study
    suspend fun addCertificate(data: List<CertificateModel>)

    suspend fun getCertificates(): List<CertificateModel>

    suspend fun deleteCertificates()

    suspend fun getCertificatesPlaces(): List<NewCertificatePlacesModel>

    suspend fun deleteCertificatesPlaces()

    suspend fun addCertificatePlaces(data: List<NewCertificatePlacesModel>)

    suspend fun addMarkSheet(data: List<MarkSheetModel>)

    suspend fun getMarkSheet(): List<MarkSheetModel>

    suspend fun deleteMarkSheet()
}