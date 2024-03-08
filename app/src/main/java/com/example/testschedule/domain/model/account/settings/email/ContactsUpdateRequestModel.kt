package com.example.testschedule.domain.model.account.settings.email

import com.example.testschedule.data.remote.dto.account.settings.email.ContactsUpdateRequestDto

data class ContactsUpdateRequestModel(
    val contactValue: String, // sa****n@****.com
    val id: Int // 8****9
) {
    fun toDto() = ContactsUpdateRequestDto(
        contactValue = contactValue,
        id = id
    )
}