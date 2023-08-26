package com.example.testschedule.data.local.entity.account.penalty

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testschedule.domain.model.account.penalty.PenaltyModel

@Entity
data class PenaltyEntity(
    @PrimaryKey
    val id: Int,           // 6914
    val date: String,     // 22.11.2022
    val reason: String,  // Правонарушение
    val status: String, // Снят
    val type: String,  // Противоправные действия
    val note: String  // Постановление КДН администрации Московского района г.Минска ...
) {
    fun toModel() = PenaltyModel(
        id = this.id,
        date = this.date,
        reason = this.reason,
        status = this.status,
        type = this.type,
        note = this.note
    )
}