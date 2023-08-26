package com.example.testschedule.data.remote.dto.account.penalty

import com.example.testschedule.domain.model.account.penalty.PenaltyModel

data class PenaltyDto(
    val directiveDto: Directive?,
    val id: Int, // 6914
    val note: String?, // Постановление КДН администрации Московского района г.Минска от 24.11.2022 №40-1087/2022/1 (исх. от 05.12.2022 №8-1037) ч.1 ст. 19.3 КоАП РБ (распитие)
    val reason: String?, // Правонарушение
    val status: String? // Снят
) {
    data class Directive(
        val date: String?, // 13.12.2022
        val eventType: String?, // Противоправные действия
        val eventTypeName: String?, // PENALTY_SPPS
        val id: Int?, // 66689
        val number: String?, // 6593
        val type: String?, // Другое
        val typeName: String? // Противоправные действия
    )

    fun toModel() = PenaltyModel(
        id = this.id,
        reason = this.reason ?: "",
        date = this.directiveDto?.date ?: "",
        status = this.status ?: "",
        type = this.directiveDto?.typeName ?: "",
        note = this.note ?: ""
    )
}