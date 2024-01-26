package com.example.testschedule.domain.model.account.study.certificate

import com.example.testschedule.data.local.entity.account.study.certificate.CertificateEntity

/**
 * [certificateType] - Тип печати / обычная \ гербовая
 *
 * [dateOrder] - Дата заказа справки
 *
 * [id] - ID справки
 *
 * [issueDate] - Дата готовности справки
 *
 * [number] - Номер справки
 *
 * [provisionPlace] - Место, куда нужна справка
 *
 * [rejectionReason] - Почему была отменена справка
 *
 * [status] - Статус
 */
data class CertificateModel(
    /** обычная */
    val certificateType: String,
    /** 24.10.2023 */
    val dateOrder: String,
    /** 141598 */
    val id: Int,
    /** 25.10.2023 */
    val issueDate: String?,
    /** 1867 */
    val number: Int,
    /** по месту работы родителей */
    val provisionPlace: String,
    /** "По желанию студента" */
    val rejectionReason: String?,
    /**
     * Status:
     *
     * 1 -> "Напечатано"
     *
     * 2 -> "Обработка"
     *
     * 3 -> "Отклонена"
     */
    val status: Int
) {
    fun toEntity() = CertificateEntity(
        certificateType = this.certificateType,
        dateOrder = dateOrder,
        id = id,
        issueDate = issueDate,
        number = number,
        provisionPlace = provisionPlace,
        rejectionReason = rejectionReason,
        status = status
    )
}