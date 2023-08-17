package com.example.testschedule.domain.model.schedule

import com.example.testschedule.data.local.entity.schedule.ListOfEmployeesEntity

data class ListOfEmployeesModel(
    val academicDepartment: List<String>, // ["каф. фиб"]
    val fio: String, // Абрамов И. И. (профессор)
    val firstName: String, // Игорь
    val lastName: String, // Абрамов
    val middleName: String?, // Иванович
    val photoLink: String?, // https://iis.bsuir.by/api/v1/employees/photo/500434
    val rank: String?, // профессор
    val urlId: String // i-abramov
) {
    fun toEntity() : ListOfEmployeesEntity = ListOfEmployeesEntity(
        this.academicDepartment,
        this.fio,
        this.firstName,
        this.lastName,
        this.middleName,
        this.photoLink,
        this.rank,
        this.urlId
    )
}