package com.example.testschedule.data.remote.dto.account.dormitory

import com.example.testschedule.domain.model.account.dormitory.DormitoryModel

data class DormitoryDto(
    val acceptedDate: Long?, // Поставлен в очередь 1687784848565
    val applicationDate: Long?, // Дата подачи 1687782804333
    val docContent: Any?, // ХЗ null
    val docReference: String?, // Название дока 16496.pdf
    val id: Int, // 17710
    val number: Int?, // Номер заявки 7996
    val numberInQueue: Any?, // Хз null
    val rejectionReason: String?, // "Плохой мальчик"
    val roomInfo: String?, // 610-а, Общ.4
    val settledDate: Long?, // Дата заселения 1661159041689
    val status: String? // Документы приняты | Ожидание | К заселению | Заселён | Отклонена | Выселен
) {
    fun toModel() = DormitoryModel(
        acceptedDate = this.acceptedDate,
        applicationDate = this.applicationDate,
        docContent = this.docContent,
        docReference = this.docReference,
        id = this.id,
        number = this.number,
        numberInQueue = this.numberInQueue,
        rejectionReason = this.rejectionReason,
        roomInfo = this.roomInfo,
        settledDate = this.settledDate,
        status = this.status,
    )
}