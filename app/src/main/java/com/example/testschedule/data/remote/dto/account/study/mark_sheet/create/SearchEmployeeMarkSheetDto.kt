package com.example.testschedule.data.remote.dto.account.study.mark_sheet.create

import com.example.testschedule.domain.model.account.study.mark_sheet.create.SearchEmployeeMarkSheetModel

data class SearchEmployeeMarkSheetDto(
    val academicDepartment: String?, // "каф информатики"
    val fio: String?, // Стройникова Е. Д.
    val firstName: String?, // Елена
    val id: Int?, // 500740
    val lastName: String?, // Стройникова
    val middleName: String?, // Дмитриевна
    val price: Double? // 5.75
) {
    fun toModel() = SearchEmployeeMarkSheetModel(
        academicDepartment = academicDepartment,
        fio = fio ?: "",
        firstName = firstName,
        id = id,
        lastName = lastName,
        middleName = middleName,
        price = price
    )
}