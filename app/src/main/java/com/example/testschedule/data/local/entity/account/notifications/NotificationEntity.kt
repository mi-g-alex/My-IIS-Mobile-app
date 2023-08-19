package com.example.testschedule.data.local.entity.account.notifications

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testschedule.domain.model.account.notifications.NotificationModel

@Entity
data class NotificationEntity(
    @PrimaryKey
    val id: Int, // 1943
    val date: String, // 14.07.2023 15:21:55
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