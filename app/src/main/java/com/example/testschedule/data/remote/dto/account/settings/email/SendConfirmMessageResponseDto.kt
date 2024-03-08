package com.example.testschedule.data.remote.dto.account.settings.email

import com.example.testschedule.domain.model.account.settings.email.SendConfirmMessageResponseModel

data class SendConfirmMessageResponseDto(
    val codeExpiredTime: String // 2024-03-05T16:42:52.287Z
) {
    fun toModel() = SendConfirmMessageResponseModel(
        codeExpiredTime = codeExpiredTime
    )
}