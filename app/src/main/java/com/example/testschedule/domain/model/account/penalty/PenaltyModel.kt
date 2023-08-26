package com.example.testschedule.domain.model.account.penalty

import com.example.testschedule.data.local.entity.account.penalty.PenaltyEntity

data class PenaltyModel(
    val id: Int,           // 6914
    val date: String,     // 22.11.2022
    val reason: String,  // Правонарушение
    val status: String, // Снят
    val type: String,  // Противоправные действия
    val note: String  // Постановление КДН администрации Московского района г.Минска от 24.11.2022 №40-1087/2022/1 (исх. от 05.12.2022 №8-1037) ч.1 ст. 19.3 КоАП РБ (распитие)
) {
    fun toEntity() = PenaltyEntity(
        id = this.id,
        date = this.date,
        reason = this.reason,
        status = this.status,
        type = this.type,
        note = this.note
    )
}
