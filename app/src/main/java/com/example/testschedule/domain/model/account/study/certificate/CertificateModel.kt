package com.example.testschedule.domain.model.account.study.certificate

import com.example.testschedule.data.local.entity.account.study.certificate.CertificateEntity


/**
 * Status:
 *
 * 1 -> "Напечатано"
 *
 * 2 -> "Обработка"
 *
 * 3 -> "Отклонена"
 */
data class CertificateModel(
    val certificateType: String, // обычная
    val dateOrder: String, // 24.10.2023
    val id: Int, // 141598
    val issueDate: String?, // 25.10.2023
    val number: Int, // 1867
    val provisionPlace: String, // по месту работы родителей
    val rejectionReason: String?, // "По желанию студента"
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