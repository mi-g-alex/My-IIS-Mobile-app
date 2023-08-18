package com.example.testschedule.domain.model.account.notifications

data class NotificationModel(
    val date: String, // 14.07.2023 15:21:55
    val id: Int, // 1943
    val isViewed: Boolean, // true
    val message: String, // Ваша заявка на заселение в общежитие была одобрена.
    val type: String // SUCCESS | FAILURE
)
