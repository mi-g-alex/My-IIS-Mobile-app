package com.example.testschedule.data.remote.dto.account.study.certificate

data class CreateCertificateDto (
    val certificateCount: Int, // 1
    val certificateRequestDto: CreateCertificateItemDto?
) {
    data class CreateCertificateItemDto(
        val certificateType: String, // обычная
        val provisionPlace: String // по месту работы родителей
    )
}