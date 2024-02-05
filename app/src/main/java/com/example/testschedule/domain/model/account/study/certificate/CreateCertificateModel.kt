package com.example.testschedule.domain.model.account.study.certificate

import com.example.testschedule.data.remote.dto.account.study.certificate.CreateCertificateDto

data class CreateCertificateModel (
    val count: Int, // 1
    val certificate: CreateCertificateItemModel
) {
    data class CreateCertificateItemModel(
        val certificateType: String, // обычная
        val provisionPlace: String // по месту работы родителей
    ) {
        fun toDto() = CreateCertificateDto.CreateCertificateItemDto(
            certificateType, provisionPlace
        )
    }

    fun toDto() = CreateCertificateDto(
        certificateCount = count,
        certificateRequestDto = certificate.toDto()
    )
}