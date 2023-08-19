package com.example.testschedule.data.remote.dto.account.notifications

import com.example.testschedule.domain.model.account.notifications.NotificationModel

data class NotificationsDto(
    val hasNext: Boolean, // false
    val notifications: List<NotificationDto>,
    val totalElements: Int // 2
) {
    data class NotificationDto(
        val date: String, // 14.07.2023 15:21:55
        val id: Int, // 1943
        val isViewed: Boolean, // true
        val message: String, // Ваша заявка на заселение в общежитие была одобрена.
        val type: String // SUCCESS | FAILURE | INFO
    ) {
        fun toModel() = NotificationModel(
            date = this.date,
            id = this.id,
            isViewed = this.isViewed,
            message = this.message,
            type = this.type
        )
    }
}