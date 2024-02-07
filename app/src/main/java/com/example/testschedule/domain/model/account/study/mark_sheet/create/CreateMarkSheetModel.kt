package com.example.testschedule.domain.model.account.study.mark_sheet.create

import com.example.testschedule.data.remote.dto.account.study.mark_sheet.additional.MarkSheetTypeModel
import com.example.testschedule.data.remote.dto.account.study.mark_sheet.create.CreateMarkSheetDto

data class CreateMarkSheetModel(
    val price: Double,
    val markSheetType: MarkSheetTypeModel,
    val reason: Int, // 1 - уваж, 2 - не уваж
    val hours: String,
    val subject: SubjectModel,
    val absentDate: String, // "22.12.2022"
    val employee: SearchEmployeeMarkSheetModel
) {
    data class SubjectModel(
        val focsId: Int?,
        val thId: Int?
    ) {
        fun toDto() = CreateMarkSheetDto.SubjectDto(
            focsId = focsId, thId = thId
        )
    }

    fun toDto() = CreateMarkSheetDto(
        subject = subject.toDto(),
        price = price,
        markSheetType = markSheetType.toDto(),
        reason = reason,
        hours = hours,
        absentDate = absentDate,
        employee = employee.toDto()
    )
}
