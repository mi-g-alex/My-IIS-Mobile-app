package com.example.testschedule.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testschedule.data.local.entity.account.announcement.AnnouncementEntity
import com.example.testschedule.data.local.entity.account.group.GroupEntity
import com.example.testschedule.data.local.entity.account.mark_book.MarkBookEntity
import com.example.testschedule.data.local.entity.account.notifications.NotificationEntity
import com.example.testschedule.data.local.entity.account.omissions.OmissionsEntity
import com.example.testschedule.data.local.entity.account.penalty.PenaltyEntity
import com.example.testschedule.data.local.entity.account.profile.AccountProfileEntity
import com.example.testschedule.data.local.entity.account.study.certificate.CertificateEntity
import com.example.testschedule.data.local.entity.account.study.certificate.NewCertificatePlacesEntity
import com.example.testschedule.data.local.entity.account.study.mark_sheet.MarkSheetEntity
import com.example.testschedule.data.local.entity.auth.LoginAndPasswordEntity
import com.example.testschedule.data.local.entity.auth.UserBasicDataEntity
import com.example.testschedule.data.local.entity.schedule.ListOfEmployeesEntity
import com.example.testschedule.data.local.entity.schedule.ListOfGroupsEntity
import com.example.testschedule.data.local.entity.schedule.ListOfSavedEntity
import com.example.testschedule.data.local.entity.schedule.ScheduleEntity
import com.example.testschedule.domain.model.schedule.ScheduleModel

@Dao
interface UserDao {

    // Schedule
    @Query("SELECT * FROM ScheduleEntity WHERE id = :id")
    suspend fun getSchedule(id: String): ScheduleModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setSchedule(model: ScheduleEntity)

    @Query("DELETE FROM ScheduleEntity WHERE id = :id")
    suspend fun deleteSchedule(id: String)

    @Query("SELECT * FROM ListOfSavedEntity")
    suspend fun getAllSavedScheduleList(): List<ListOfSavedEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewSavedScheduleToList(model: ListOfSavedEntity)

    @Query("DELETE FROM ListOfSavedEntity WHERE id = :id")
    suspend fun deleteFromSavedScheduleList(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllGroupsList(group: List<ListOfGroupsEntity>)

    @Query("SELECT * FROM ListOfGroupsEntity WHERE name= :name")
    suspend fun getGroupById(name: String): ListOfGroupsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEmployeesList(employee: List<ListOfEmployeesEntity>)

    @Query("SELECT * FROM ListOfEmployeesEntity WHERE urlId= :id")
    suspend fun getEmployeeById(id: String): ListOfEmployeesEntity?

    @Query("SELECT * FROM ListOfGroupsEntity")
    suspend fun getAllGroupsList(): List<ListOfGroupsEntity>

    @Query("SELECT * FROM ListOfEmployeesEntity")
    suspend fun getAllEmployeesList(): List<ListOfEmployeesEntity>

    @Query("DELETE FROM ListOfGroupsEntity")
    suspend fun deleteAllGroupsList()

    @Query("DELETE FROM ListOfEmployeesEntity")
    suspend fun deleteAllEmployeesList()


    // User Auth
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setLoginAndPassword(data: LoginAndPasswordEntity)

    @Query("SELECT * FROM LoginAndPasswordEntity LIMIT 1")
    suspend fun getLoginAndPassword(): LoginAndPasswordEntity

    @Query("DELETE FROM LoginAndPasswordEntity WHERE `key`=0")
    suspend fun deleteLoginAndPassword()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setUserBasicData(data: UserBasicDataEntity)

    @Query("SELECT * FROM UserBasicDataEntity WHERE `key`=0")
    suspend fun getUserBasicData(): UserBasicDataEntity?

    @Query("DELETE FROM UserBasicDataEntity WHERE `key`=0")
    suspend fun deleteUserBasicData()


    // Account

    // Profile
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setAccountProfile(data: AccountProfileEntity)

    @Query("SELECT * FROM AccountProfileEntity WHERE `key`=0")
    suspend fun getAccountProfile(): AccountProfileEntity?

    @Query("DELETE FROM AccountProfileEntity WHERE `key`=0")
    suspend fun deleteAccountProfile()

    // Notification

    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun addNotification(data: List<NotificationEntity>)

    @Query("UPDATE NotificationEntity SET isViewed=:status WHERE id=:id")
    suspend fun updateNotificationStatus(id: Int, status: Boolean = true)

    @Query("SELECT * FROM NotificationEntity")
    suspend fun getNotifications(): List<NotificationEntity>

    @Query("DELETE FROM NotificationEntity")
    suspend fun deleteNotifications()

    // Group
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setUserGroup(data: GroupEntity)

    @Query("SELECT * FROM GroupEntity WHERE `key`=0")
    suspend fun getUserGroup(): GroupEntity?

    @Query("DELETE FROM GroupEntity")
    suspend fun deleteUserGroup()

    // Mark book
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setMarkBook(data: MarkBookEntity)

    @Query("SELECT * FROM MarkBookEntity WHERE `key`=0")
    suspend fun getMarkBook(): MarkBookEntity?

    @Query("DELETE FROM MarkBookEntity")
    suspend fun deleteMarkBook()

    // Omissions
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOmissions(data: List<OmissionsEntity>)

    @Query("SELECT * FROM OmissionsEntity")
    suspend fun getOmissions(): List<OmissionsEntity>?

    @Query("DELETE FROM OmissionsEntity")
    suspend fun deleteOmissions()

    // Penalty
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPenalty(data: List<PenaltyEntity>)

    @Query("SELECT * FROM PenaltyEntity")
    suspend fun getPenalty(): List<PenaltyEntity>?

    @Query("DELETE FROM PenaltyEntity")
    suspend fun deletePenalty()

    // Announcements
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAnnouncement(data: List<AnnouncementEntity>)

    @Query("SELECT * FROM AnnouncementEntity")
    suspend fun getAnnouncements(): List<AnnouncementEntity>

    @Query("DELETE FROM AnnouncementEntity")
    suspend fun deleteAnnouncements()

    // Study
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCertificate(data: List<CertificateEntity>)

    @Query("SELECT * FROM CertificateEntity")
    suspend fun getCertificates(): List<CertificateEntity>

    @Query("DELETE FROM CertificateEntity")
    suspend fun deleteCertificates()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCertificatePlaces(data: List<NewCertificatePlacesEntity>)

    @Query("SELECT * FROM NewCertificatePlacesEntity")
    suspend fun getCertificatesPlaces(): List<NewCertificatePlacesEntity>

    @Query("DELETE FROM NewCertificatePlacesEntity")
    suspend fun deleteCertificatesPlaces()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMarkSheet(data: List<MarkSheetEntity>)

    @Query("SELECT * FROM MarkSheetEntity")
    suspend fun getMarkSheet(): List<MarkSheetEntity>

    @Query("DELETE FROM MarkSheetEntity")
    suspend fun deleteMarkSheet()
}