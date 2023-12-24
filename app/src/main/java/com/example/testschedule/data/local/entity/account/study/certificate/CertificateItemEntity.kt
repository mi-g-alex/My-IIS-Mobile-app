package com.example.testschedule.data.local.entity.account.study.certificate

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testschedule.domain.model.account.study.certificate.CertificateModel

@Entity
data class CertificateEntity(
    val certificateType: String, // обычная
    val dateOrder: String, // 24.10.2023
    @PrimaryKey
    val id: Int, // 141598
    val issueDate: String?, // 25.10.2023
    val number: Int, // 1867
    val provisionPlace: String, // по месту работы родителей
    val rejectionReason: String?, // "По желанию студента"
    val status: Int
) {
    fun toModel() = CertificateModel(
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