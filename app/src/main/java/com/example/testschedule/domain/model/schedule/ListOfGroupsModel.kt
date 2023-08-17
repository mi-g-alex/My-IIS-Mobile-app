package com.example.testschedule.domain.model.schedule

import com.example.testschedule.data.local.entity.schedule.ListOfGroupsEntity

data class ListOfGroupsModel(
    val course: Int, // 3
    val facultyAbbrev: String, // ФКП
    val name: String, // 010101
    val specialityAbbrev: String, // ИСиТ(в ОПБ)
    val specialityName: String // Информационные системы и технологии (по направлениям)
) {
    fun toEntity() : ListOfGroupsEntity = ListOfGroupsEntity(
        this.course,
        this.facultyAbbrev,
        this.name,
        this.specialityAbbrev,
        this.specialityName
    )
}
