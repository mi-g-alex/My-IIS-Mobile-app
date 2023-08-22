package com.example.testschedule.data.local.entity.account.omissions

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testschedule.domain.model.account.omissions.OmissionsModel

@Entity
data class OmissionsEntity(
    @PrimaryKey
    val id: Int, // 29974
    val dateFrom: Long, // 1680901200000
    val dateTo: Long, // 1681160400000
    val name: String, // Заявление по ОРВИ
    val note: String, // Участие в полуфинале BSUIR Open 2023
    val term: String // 2
) {
    fun toModel() = OmissionsModel(
        id = this.id,
        dateFrom = this.dateFrom,
        dateTo = this.dateTo,
        name = this.name,
        note = this.note,
        term = this.term
    )
}
