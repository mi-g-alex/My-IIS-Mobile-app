package com.example.testschedule.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testschedule.data.local.entity.ListOfEmployeesEntity
import com.example.testschedule.data.local.entity.ListOfGroupsEntity
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
    suspend fun deleteFromSavedScheduleList(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllGroupsList(group: ListOfGroupsEntity)

    @Query("SELECT * FROM ListOfGroupsEntity WHERE name= :name")
    suspend fun getGroupById(name: String) : ListOfGroupsEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEmployeesList(employee: ListOfEmployeesEntity)

    @Query("SELECT * FROM ListOfEmployeesEntity WHERE urlId= :id")
    suspend fun getEmployeeById(id: String) : ListOfEmployeesEntity

    @Query("SELECT * FROM ListOfGroupsEntity")
    suspend fun getAllGroupsList(): List<ListOfGroupsEntity>

    @Query("SELECT * FROM ListOfEmployeesEntity")
    suspend fun getAllEmployeesList(): List<ListOfEmployeesEntity>

    @Query("DELETE FROM ListOfGroupsEntity")
    suspend fun deleteAllGroupsList()

    @Query("DELETE FROM ListOfEmployeesEntity")
    suspend fun deleteAllEmployeesList()


}