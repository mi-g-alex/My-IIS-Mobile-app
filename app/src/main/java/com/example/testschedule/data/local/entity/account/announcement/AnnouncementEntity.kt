package com.example.testschedule.data.local.entity.account.announcement

import androidx.room.Entity
import androidx.room.PrimaryKey

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
)
