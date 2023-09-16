package com.example.testschedule.data.local.entity.account.announcement

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testschedule.domain.model.account.announcement.AnnouncementModel

@Entity
data class AnnouncementEntity(
    @PrimaryKey
    val id: Int,
    val date: String?,
    val startTime: String?,
    val endTime: String?,
    val auditory: String?,
    val employee: String?,
    val content: String?,
    val urlId: String?,
) {
    fun toModel() = AnnouncementModel(
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