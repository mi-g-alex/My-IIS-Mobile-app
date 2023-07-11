package com.example.testschedule.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testschedule.data.local.entity.ListOfSavedEntity
import com.example.testschedule.data.local.entity.ScheduleEntity
import com.example.testschedule.domain.model.schedule.ScheduleModel

@Dao
interface UserDao {

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
    suspend fun deleteFromScheduleList(id: String)


}