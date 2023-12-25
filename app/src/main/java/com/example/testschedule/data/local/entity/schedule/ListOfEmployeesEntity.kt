package com.example.testschedule.data.local.entity.schedule

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel

@Entity
data class ListOfEmployeesEntity(
    val academicDepartment: List<String>, // ["каф. фиб"]
    @ColumnInfo(defaultValue = "")
    val calendarId: String, // 4t4b9qurekmm2nnb2tjjdcseq0@group.calendar.google.com
    val fio: String, // Абрамов И. И. (профессор)
    val firstName: String, // Игорь
    val lastName: String, // Абрамов
    val middleName: String?, // Иванович
    val photoLink: String?, // https://iis.bsuir.by/api/v1/employees/photo/500434
    val rank: String?, // профессор
    val degree: String?, // д.ф.-м.н.
    @PrimaryKey
    val urlId: String // i-abramov
) {
    fun toModel(): ListOfEmployeesModel = ListOfEmployeesModel(
        academicDepartment = this.academicDepartment,
        calendarId = this.calendarId,
        fio = this.fio,
        firstName = this.firstName,
        lastName = this.lastName,
        middleName = this.middleName,
        photoLink = this.photoLink,
        rank = this.rank,
        degree = this.degree,
        urlId = this.urlId
    )
}