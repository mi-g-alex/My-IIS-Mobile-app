package com.example.testschedule.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.testschedule.data.local.entity.ScheduleEntity

@Database(
    version = 1,
    entities = [
        ScheduleEntity::class
    ],
    exportSchema = true,
    autoMigrations = [

    ],
    )
@TypeConverters(Converters::class)
abstract class UserDataBase : RoomDatabase() {

    abstract val userDao: UserDao

    companion object {
        const val DATABASE_NAME = "user_db"
    }
}