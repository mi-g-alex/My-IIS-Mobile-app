package com.example.testschedule.domain.model.account.dormitory

data class DormitoryModel(
    val id: Int, // 17710
    val number: Int?, // Номер заявки 7996
    val numberInQueue: Any?, // Хз null
    val status: String?, // Документы приняты | Ожидание | К заселению | Заселён | Отклонена | Выселен
    val applicationDate: Long?, // Дата подачи 1687782804333
    val acceptedDate: Long?, // Поставлен в очередь 1687784848565
    val settledDate: Long?, // Дата заселения 1661159041689
    val roomInfo: String?, // 610-а, Общ.4
    val docContent: Any?, // ХЗ null
    val docReference: String?, // Название дока 16496.pdf
    val rejectionReason: String? // "Плохой мальчик"
)