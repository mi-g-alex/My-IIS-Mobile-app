package com.example.testschedule.domain.model.account.settings.email

import com.example.testschedule.data.remote.dto.account.settings.email.SendConfirmCodeRequestDto

data class SendConfirmCodeRequestModel(
    val code: String, // 578223
    val contactId: Int // 823779
) {
    fun toDto() = SendConfirmCodeRequestDto(
        code = code,
        contactId = contactId
    )
}
