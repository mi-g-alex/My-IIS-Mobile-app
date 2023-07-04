package com.example.testschedule.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testschedule.data.local.entity.ScheduleEntity
import com.example.testschedule.domain.modal.schedule.ScheduleModel

@Dao
interface UserDao {

    @Query("SELECT * FROM ScheduleEntity WHERE id = :id")
    suspend fun getSchedule(id: String) : ScheduleModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setSchedule(model : ScheduleEntity)


}