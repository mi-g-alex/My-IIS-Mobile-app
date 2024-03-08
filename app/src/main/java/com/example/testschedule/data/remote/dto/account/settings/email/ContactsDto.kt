package com.example.testschedule.data.remote.dto.account.settings.email

import com.example.testschedule.domain.model.account.settings.email.ContactsModel

data class ContactsDto(
    val contactDtoList: List<ContactDto>?,
    val numberOfAttempts: Int? // 3
) {
    data class ContactDto(
        val codeExpirationTime: String?, // ISOTIME
        val confirmed: Boolean, // true
        val contactTypeId: Int, // 6
        val contactValue: String, // sa****n@****.com
        val id: Int // 8****9
    ) {
        fun toModel() = ContactsModel.ContactModel(
            codeExpirationTime = codeExpirationTime,
            confirmed = confirmed,
            contactTypeId = contactTypeId,
            contactValue = contactValue,
            id = id
        )
    }

    fun toModel() = ContactsModel(
        contactDtoList = contactDtoList?.map { it.toModel() } ?: emptyList(),
        numberOfAttempts = numberOfAttempts ?: 0
    )
}