package com.example.testschedule.domain.model.account.settings.email

data class ContactsModel(
    val contactDtoList: List<ContactModel>,
    val numberOfAttempts: Int // 3
) {
    data class ContactModel(
        val codeExpirationTime: String?, // ISOTIME
        val confirmed: Boolean, // true
        val contactTypeId: Int, // 6
        val contactValue: String, // sa****n@****.com
        val id: Int // 8****9
    )
}
