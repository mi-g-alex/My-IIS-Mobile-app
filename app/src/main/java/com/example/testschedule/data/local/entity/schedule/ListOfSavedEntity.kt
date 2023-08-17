package com.example.testschedule.data.local.entity.schedule

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ListOfSavedEntity(
    @PrimaryKey
    val id: String,
    val isGroup: Boolean,
    val title : String
)