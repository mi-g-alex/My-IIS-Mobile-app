package com.example.testschedule.data.local.entity.schedule

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testschedule.domain.model.schedule.ListOfGroupsModel

@Entity
data class ListOfGroupsEntity(
    val course: Int, // 3
    @ColumnInfo(defaultValue = "")
    val calendarId: String, // 4t4b9qurekmm2nnb2tjjdcseq0@group.calendar.google.com
    val facultyAbbrev: String, // ФКП
    @PrimaryKey
    val name: String, // 010101
    val specialityAbbrev: String, // ИСиТ(в ОПБ)
    val specialityName: String // Информационные системы и технологии (по направлениям)
) {
    fun toModel(): ListOfGroupsModel = ListOfGroupsModel(
        course = this.course,
        calendarId = this.calendarId,
        facultyAbbrev = this.facultyAbbrev,
        name = this.name,
        specialityAbbrev = this.specialityAbbrev,
        specialityName = this.specialityName
    )
}
