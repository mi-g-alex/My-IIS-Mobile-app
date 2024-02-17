package com.example.testschedule.data.remote.dto.account.study.mark_sheet.create

import com.example.testschedule.data.remote.dto.account.study.mark_sheet.additional.MarkSheetTypeDto
import com.example.testschedule.domain.model.account.study.mark_sheet.create.CreateMarkSheetModel

data class CreateMarkSheetDto(
    val price: Double,
    val markSheetType: MarkSheetTypeDto,
    val reason: Int, // 1 - уваж, 2 - не уваж
    val hours: String,
    val subject: SubjectDto,
    val absentDate: String, // "22.12.2022"
    val employee: SearchEmployeeMarkSheetDto
) {
    data class SubjectDto(
        val focsId: Int?,
        val thId: Int?
    ) {
        fun toModel() = CreateMarkSheetModel.SubjectModel(
            focsId = focsId, thId = thId
        )
    }

    fun toModel() = CreateMarkSheetModel(
        subject = subject.toModel(),
        price = price,
        markSheetType = markSheetType.toModel(),
        reason = reason,
        hours = hours,
        absentDate = absentDate,
        employee = employee.toModel()
    )
}
