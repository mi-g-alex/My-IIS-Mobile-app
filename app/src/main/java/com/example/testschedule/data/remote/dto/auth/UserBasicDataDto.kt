package com.example.testschedule.data.remote.dto.auth

import com.example.testschedule.domain.model.auth.UserBasicDataModel

data class UserBasicDataDto(
    val authorities: List<Any?>,
    val canStudentNote: Boolean, // false
    val email: String?, // sashaxxxxxx@xxxxx.com
    val fio: String, // Иванов Иван Иванович
    val group: String, // 25xxxx
    val hasNotConfirmedContact: Boolean, // false
    val isGroupHead: Boolean, // false
    val phone: String, // +37529xxxxxxx
    val photoUrl: String?, // https://drive.google.com/uc?id=1w0kKMQi1eQPreMQBI2gSxm0AOc...
    val username: String, // 253500xx
) {
    fun toModel(cookie: String): UserBasicDataModel = UserBasicDataModel(
        canStudentNote = this.canStudentNote,
        email = this.email,
        fio = this.fio,
        group = this.group,
        hasNotConfirmedContact = this.hasNotConfirmedContact,
        isGroupHead = this.isGroupHead,
        phone = this.phone,
        photoUrl = this.photoUrl,
        username = this.username,
        cookie = cookie
    )
}