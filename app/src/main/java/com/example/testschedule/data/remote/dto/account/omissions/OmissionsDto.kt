package com.example.testschedule.data.remote.dto.account.omissions

import com.example.testschedule.domain.model.account.omissions.OmissionsModel

data class OmissionsDto(
    val dateFrom: Long?, // 1680901200000
    val dateTo: Long?, // 1681160400000
    val id: Int, // 29974
    val name: String?, // Заявление по ОРВИ
    val note: String?, // Участие в полуфинале BSUIR Open 2023
    val term: String? // 2
) {
    fun toModel() = OmissionsModel(
        dateFrom = this.dateFrom ?: 0L,
        dateTo = this.dateTo ?: 0L,
        id = this.id,
        name = this.name ?: "",
        note = this.note ?: "",
        term = this.term ?: ""
    )
}
