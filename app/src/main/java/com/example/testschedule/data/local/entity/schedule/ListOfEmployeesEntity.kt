package com.example.testschedule.data.local.entity.schedule

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel

@Entity
data class ListOfEmployeesEntity(
    val academicDepartment: List<String>, // ["каф. фиб"]
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
        this.academicDepartment,
        this.fio,
        this.firstName,
        this.lastName,
        this.middleName,
        this.photoLink,
        this.rank,
        this.degree,
        this.urlId
    )
}