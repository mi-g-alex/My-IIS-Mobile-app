package com.example.testschedule.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CacheUpdateEntity(
    @PrimaryKey val cacheKey: String,
    val updatedAt: Long
)
