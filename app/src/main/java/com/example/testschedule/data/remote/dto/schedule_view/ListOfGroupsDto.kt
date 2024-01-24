package com.example.testschedule.data.remote.dto.schedule_view

import com.example.testschedule.domain.model.schedule.ListOfGroupsModel

data class ListOfGroupsDto(
    val calendarId: String?, // j7v9s1pqj8e82t57gg1i8fvo4s@group.calendar.google.com
    val course: Int, // 3
    val educationDegree: Int, // 1
    val facultyAbbrev: String, // ФКП
    val facultyId: Int, // 20017
    val id: Int, // 23277
    val name: String, // 010101
    val specialityAbbrev: String, // ИСиТ(в ОПБ)
    val specialityDepartmentEducationFormId: Int, // 20011
    val specialityName: String, // Информационные системы и технологии (по направлениям)
) {
    fun toModel() = ListOfGroupsModel(
        course = this.course,
        calendarId = this.calendarId ?: "",
        facultyAbbrev = this.facultyAbbrev,
        name = this.name,
        specialityAbbrev = this.specialityAbbrev,
        specialityName = this.specialityName
    )
}