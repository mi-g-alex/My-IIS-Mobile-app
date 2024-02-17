package com.example.testschedule.domain.model.account.settings.password

import com.example.testschedule.data.remote.dto.account.settings.password.ChangePasswordDto

data class ChangePasswordModel(
    val password: String,
    val newPassword: String
) {
    fun toDto() = ChangePasswordDto(
        password,
        newPassword
    )
}