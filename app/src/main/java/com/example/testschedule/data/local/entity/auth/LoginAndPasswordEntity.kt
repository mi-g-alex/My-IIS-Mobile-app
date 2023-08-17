package com.example.testschedule.data.local.entity.auth

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LoginAndPasswordEntity(
    @PrimaryKey
    val key: Int,
    val username: String,
    val password: String
)
