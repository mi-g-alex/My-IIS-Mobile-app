package com.example.testschedule.domain.model.auth

data class UserBasicDataModel(
    val canStudentNote: Boolean, // false
    val email: String?, // sashaxxxxxx@xxxxx.com
    val fio: String, // Иванов Иван Иванович
    val group: String, // 25xxxx
    val hasNotConfirmedContact: Boolean, // false
    val isGroupHead: Boolean, // false
    val phone: String, // +37529xxxxxxx
    val photoUrl: String?, // https://drive.google.com/uc?id=1w0kKMQi1eQPreMQBI2gSxm0AOc...
    val username: String, // 253500xx
    val cookie: String,
)