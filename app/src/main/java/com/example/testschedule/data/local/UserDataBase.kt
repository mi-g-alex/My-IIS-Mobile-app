package com.example.testschedule.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.testschedule.data.local.entity.ListOfSavedEntity
import com.example.testschedule.data.local.entity.ScheduleEntity

@Database(
    version = 2,
    entities = [
        ScheduleEntity::class,
        ListOfSavedEntity::class,
    ],
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
    ],
)
@TypeConverters(Converters::class)
abstract class UserDataBase : RoomDatabase() {

    abstract val userDao: UserDao

    companion object {
        const val DATABASE_NAME = "user_db"
    }
}