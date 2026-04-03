package com.example.testschedule.data.remote.dto.account.omissions

import com.example.testschedule.domain.model.account.omissions.OmissionsModel
import java.text.SimpleDateFormat
import java.util.Locale

data class OmissionsDto(
    val faculty: String?,
    val omissionDtoList: List<OmissionsDtoList>?
) {
    data class OmissionsDtoList(
        val dateFrom: String?, // 1680901200000
        val dateTo: String?, // 1681160400000
        val id: Int, // 29974
        val name: String?, // Заявление по ОРВИ
        val note: String?, // Участие в полуфинале BSUIR Open 2023
        val term: String? // 2
    ) {
        private fun String?.toTimestamp(): Long {
            if (this == null) return 0L
            return try {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(this)?.time ?: 0L
            } catch (e: Exception) {
                0L
            }
        }

        fun toModel() = OmissionsModel(
            dateFrom = this.dateFrom.toTimestamp() ?: 0L,
            dateTo = this.dateTo.toTimestamp() ?: 0L,
            id = this.id,
            name = this.name ?: "",
            note = this.note ?: "",
            term = this.term ?: ""
        )
    }
}
