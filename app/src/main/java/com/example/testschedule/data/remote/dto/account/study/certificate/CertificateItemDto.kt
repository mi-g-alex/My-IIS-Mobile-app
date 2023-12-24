package com.example.testschedule.data.remote.dto.account.study.certificate

import com.example.testschedule.domain.model.account.study.certificate.CertificateModel

data class CertificateItemDto(
    val certificateType: String?, // обычная
    val dateOrder: String?, // 24.10.2023
    val id: Int?, // 141598
    val issueDate: String?, // 25.10.2023 // Дата когда написано
    val number: Int?, // 1867
    val provisionPlace: String?, // по месту работы родителей
    val rejectionReason: String?, // "По желанию студента"
    val status: Int?
    /*
        1 -> "Напечатано"
        2 -> "Обработка"
        3 -> "Отклонена"
    */
) {
    fun toModel() = CertificateModel(
        certificateType = this.certificateType ?: "",
        dateOrder = dateOrder ?: "",
        id = id ?: 0,
        issueDate = issueDate,
        number = number ?: 0,
        provisionPlace = provisionPlace ?: "",
        rejectionReason = rejectionReason,
        status = if (status == null || status > 3) 0 else status
    )
}