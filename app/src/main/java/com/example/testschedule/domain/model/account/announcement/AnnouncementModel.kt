package com.example.testschedule.domain.model.account.announcement

import com.example.testschedule.data.local.entity.account.announcement.AnnouncementEntity

data class AnnouncementModel(
    val id: Int,
    val date: String?,
    val startTime: String?,
    val endTime: String?,
    val auditory: String?,
    val employee: String?,
    val content: String?,
    val urlId: String?,
) {
    fun toEntity() = AnnouncementEntity(
        id = this.id,
        date = this.date,
        startTime = this.startTime,
        endTime = this.endTime,
        auditory = this.auditory,
        employee = this.employee,
        content = this.content,
        urlId = this.urlId,
    )
}