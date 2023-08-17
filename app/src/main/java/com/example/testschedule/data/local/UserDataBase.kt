package com.example.testschedule.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.testschedule.data.local.entity.auth.LoginAndPasswordEntity
import com.example.testschedule.data.local.entity.auth.UserBasicDataEntity
import com.example.testschedule.data.local.entity.schedule.ListOfEmployeesEntity
import com.example.testschedule.data.local.entity.schedule.ListOfGroupsEntity
import com.example.testschedule.data.local.entity.schedule.ListOfSavedEntity
import com.example.testschedule.data.local.entity.schedule.ScheduleEntity

@Database(
    version = 4,
    entities = [
        // Schedules
        ScheduleEntity::class,
        ListOfSavedEntity::class,
        ListOfGroupsEntity::class,
        ListOfEmployeesEntity::class,
        // UserAuth
        LoginAndPasswordEntity::class,
        UserBasicDataEntity::class,
    ],
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
    ],
)
@TypeConverters(Converters::class)
abstract class UserDataBase : RoomDatabase() {

    abstract val userDao: UserDao

    companion object {
        const val DATABASE_NAME = "user_db"
    }
}