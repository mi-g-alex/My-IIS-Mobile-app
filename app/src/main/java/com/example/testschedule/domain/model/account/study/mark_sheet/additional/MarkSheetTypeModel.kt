package com.example.testschedule.data.remote.dto.account.study.mark_sheet.additional

data class MarkSheetTypeModel(
    val coefficient: Double?, // 1.0
    val fullName: String?, // 2312312
    val id: Int?, // 3
    val isCourseWork: Boolean, // false
    val isExam: Boolean, // false
    val isLab: Boolean, // true
    val isOffset: Boolean, // false
    val isRemote: Boolean, // false
    val price: Double?, // null // Всегда NULL
    val shortName: String // Лаб. работа (дн., веч., заоч.)
) {
    fun toDto() = MarkSheetTypeDto(
        coefficient = coefficient,
        fullName = fullName,
        id = id,
        isCourseWork = isCourseWork,
        isExam = isExam,
        isLab = isLab,
        isOffset = isOffset,
        isRemote = isRemote,
        price = price,
        shortName = shortName
    )
}