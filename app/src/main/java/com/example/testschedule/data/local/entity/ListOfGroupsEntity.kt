package com.example.testschedule.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testschedule.domain.model.schedule.ListOfGroupsModel

@Entity
data class ListOfGroupsEntity(
    val course: Int, // 3
    val facultyAbbrev: String, // ФКП
    @PrimaryKey
    val name: String, // 010101
    val specialityAbbrev: String, // ИСиТ(в ОПБ)
    val specialityName: String // Информационные системы и технологии (по направлениям)
) {
    fun toModel(): ListOfGroupsModel = ListOfGroupsModel(
        this.course,
        this.facultyAbbrev,
        this.name,
        this.specialityAbbrev,
        this.specialityName
    )
}
