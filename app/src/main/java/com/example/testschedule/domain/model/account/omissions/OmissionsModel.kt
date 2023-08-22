package com.example.testschedule.domain.model.account.omissions

import com.example.testschedule.data.local.entity.account.omissions.OmissionsEntity

data class OmissionsModel(
    val dateFrom: Long, // 1680901200000
    val dateTo: Long, // 1681160400000
    val id: Int, // 29974
    val name: String, // Заявление по ОРВИ
    val note: String, // Участие в полуфинале BSUIR Open 2023
    val term: String // 2
) {
    fun toEntity() = OmissionsEntity(
        id = this.id,
        dateFrom = this.dateFrom,
        dateTo = this.dateTo,
        name = this.name,
        note = this.note,
        term = this.term
    )
}
