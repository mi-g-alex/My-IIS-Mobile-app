package com.example.testschedule.data.remote.dto.account.study.mark_sheet

import com.example.testschedule.data.remote.dto.account.study.mark_sheet.additional.MarkSheetTypeDto
import com.example.testschedule.domain.model.account.study.mark_sheet.MarkSheetModel

data class MarkSheetItemDto(
    val absentDate: String?, // 27.12.2023 // Дата на какое ведомость
    val certificate: Boolean?, // false // ХЗ
    val createDate: String?, // 23.12.2023 // Дата подачи заявки
    val employee: EmployeeDto?, // Инфа о преподе
    val expireDate: String?, // Годна до
    val hours: Double?, // 4 // Сколько часов закрывает
    val id: Int, // 77205 // Просто ID
    val markSheetType: MarkSheetTypeDto?, // Инфа о ведомости общая
    val number: String?, // 435/0407 // Номер
    val paymentFormMap: Any?, // null // ХЗ
    val price: Double?, // 23.0 // ИТОГО
    val reason: Int?, // 2 // Типо норм
    val rejectionReason: String?, // Отозвана cтудентом
    val requestValidationDoc: Boolean?, // надо ли док, подтвержающий оф
    val retakeCount: Int?, // Кол-во пересдач
    val status: String?, // напечатана
    val subject: SubjectDto?,
    val term: Int? // 1
) {
    data class EmployeeDto(
        val academicDepartment: String?, // Каф.МПСС, Каф.информатики
        val fio: String?, // Владымцев В. Д.
        val firstName: String?, // Вадим
        val id: Int?, // 536343
        val lastName: String?, // Владымцев
        val middleName: String?, // Денисович
        val price: Double? // 5.75
    )

    data class SubjectDto(
        val abbrev: String?, // ОАиП
        val focsId: Int?, // Если экз\зачёт то INT
        val id: Int?, // 20662
        val lessonTypeAbbrev: String?, // ЛР
        val name: String?, // Основы алгоритмизации и программирования
        val term: Int?, // 1 // Сем
        val thId: Int? // 693131 / если лр
    )

    fun toModel() = MarkSheetModel(
        absentDate = absentDate ?: "",
        createDate = createDate ?: "",
        employeeFIO = employee?.fio ?: "",
        expireDate = expireDate ?: "",
        hours = hours ?: 0.0,
        id = id,
        type = markSheetType?.id ?: 0,
        number = number ?: "",
        price = price ?: 0.0,
        isGoodReason = reason == 1,
        rejectionReason = rejectionReason ?: "",
        retakeCount = retakeCount ?: 0,
        status = status ?: "",
        subjectName = subject?.abbrev ?: subject?.name ?: "",
        subjectType = subject?.lessonTypeAbbrev ?: "",
        term = term ?: 0
    )
}
