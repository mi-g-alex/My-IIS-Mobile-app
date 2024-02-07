package com.example.testschedule.data.local.entity.account.study.mark_sheet

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testschedule.domain.model.account.study.mark_sheet.MarkSheetModel

@Entity
data class MarkSheetEntity(
    /** Дата, на которую выписывается ведомость **/
    val absentDate: String,
    /** Дата создания ведомости **/
    val createDate: String,
    /** ФИО преподавателя **/
    val employeeFIO: String,
    /** До какого числа действует ведомость **/
    val expireDate: String,
    /** Сколько часов пропуском покрывает **/
    val hours: Double,
    /** ID **/
    @PrimaryKey
    val id: Int,
    /** TYPE: 1-экз\дифзач | 2-зачёт | 3-ЛР | 4-КР | 5-экз\дифзач дист | 6-ЛР дист **/
    val type: Int,
    /** Номер ведомости (типа 435/0407) **/
    val number: String,
    /** Сколько почек стоит **/
    val price: Double,
    /** Уваж или Неуваж **/
    val isGoodReason: Boolean,
    /** Причина отказа печатать **/
    val rejectionReason: String,
    /** Сколько пересдач **/
    val retakeCount: Int,
    /** В каком статусе **/
    val status: String,
    /** Имя предмета, кратко если есть, иначе полное **/
    val subjectName: String,
    /** тип предмета (лр и т.п.) **/
    val subjectType: String,
    /** семестр **/
    val term: Int
) {
    fun toModel() = MarkSheetModel(
        absentDate = absentDate,
        createDate = createDate,
        employeeFIO = employeeFIO,
        expireDate = expireDate,
        hours = hours,
        id = id,
        type = type,
        number = number,
        price = price,
        isGoodReason = isGoodReason,
        rejectionReason = rejectionReason,
        retakeCount = retakeCount,
        status = status,
        subjectName = subjectName,
        subjectType = subjectType,
        term = term
    )
}