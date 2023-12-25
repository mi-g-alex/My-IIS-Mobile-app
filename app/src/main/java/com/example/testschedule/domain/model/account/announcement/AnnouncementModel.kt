package com.example.testschedule.domain.model.account.announcement

import com.example.testschedule.data.local.entity.account.announcement.AnnouncementEntity

/**
 * ### AnnouncementModel - события, которые относятся к группе студента или самому студенту
 * * [id] (_1234_) - ID события
 * * [date] (_"12.09.2021"_) - дата проведения события
 * * [startTime] (_"12:25"_) - время начала события
 * * [endTime] (_"13:25"_) - время конца события
 * * [auditory] (_"4-4к."_) - место проведения события
 * * [employee] (_"Иванов И. И."_) - ФИО преподавателя
 * * [content] (_"Зачет по КПрог"_) - название события
 * * [urlId] (_"i-ivanov"_) - id, чтобы открыть расписание
 */
data class AnnouncementModel(
    val id: Int,
    val date: String?,
    val startTime: String?,
    val endTime: String?,
    val auditory: String?,
    val employee: String?,
    val content: String?,
    val urlId: String?,
) {
    fun toEntity() = AnnouncementEntity(
        id = this.id,
        date = this.date,
        startTime = this.startTime,
        endTime = this.endTime,
        auditory = this.auditory,
        employee = this.employee,
        content = this.content,
        urlId = this.urlId,
    )
}