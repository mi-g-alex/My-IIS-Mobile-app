package com.example.testschedule.data.remote.dto.account.announcement

import com.example.testschedule.domain.model.account.announcement.AnnouncementModel

data class AnnouncementDto(
    val id: Int,
    val studentGroups: List<StudentGroup>?,
    val employee: String?,
    val startTime: String?,
    val endTime: String?,
    val content: String?,
    val auditory: String?,
    val date: String?,
    val urlId: String?,
    val employeeDepartments: List<String>?
) {
    data class StudentGroup(
        val id: Int,
        val name: String,
        val specialityAbbrev: String
    )

    fun toModel() = AnnouncementModel(
        id = this.id,
        employee = this.employee,
        startTime = this.startTime,
        endTime = this.endTime,
        content = this.content,
        auditory = this.auditory,
        date = this.date,
        urlId = this.urlId
    )
}