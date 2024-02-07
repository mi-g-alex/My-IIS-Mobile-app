package com.example.testschedule.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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

@Database(
    version = 17,
    entities = [
        // Schedules
        ScheduleEntity::class,
        ListOfSavedEntity::class,
        ListOfGroupsEntity::class,
        ListOfEmployeesEntity::class,
        // UserAuth
        LoginAndPasswordEntity::class,
        UserBasicDataEntity::class,
        // Profile
        AccountProfileEntity::class,
        // Notifications
        NotificationEntity::class,
        // Group
        GroupEntity::class,
        // Mark book
        MarkBookEntity::class,
        // Omissions
        OmissionsEntity::class,
        // Penalty
        PenaltyEntity::class,
        // Announcements
        AnnouncementEntity::class,
        // Study
        CertificateEntity::class,
        NewCertificatePlacesEntity::class,
        MarkSheetEntity::class
    ],
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7),
        AutoMigration(from = 7, to = 8),
        AutoMigration(from = 8, to = 9),
        AutoMigration(from = 9, to = 10),
        AutoMigration(from = 10, to = 11),
        AutoMigration(from = 11, to = 12),
        AutoMigration(from = 12, to = 13),
        AutoMigration(from = 13, to = 14),
        AutoMigration(from = 14, to = 15),
        AutoMigration(from = 15, to = 16),
        AutoMigration(from = 16, to = 17),
    ],
)
@TypeConverters(Converters::class)
abstract class UserDataBase : RoomDatabase() {

    abstract val userDao: UserDao

    companion object {
        const val DATABASE_NAME = "user_db"
    }
}