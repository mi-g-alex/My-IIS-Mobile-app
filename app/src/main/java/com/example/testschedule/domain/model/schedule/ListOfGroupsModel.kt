package com.example.testschedule.domain.model.schedule

import com.example.testschedule.data.local.entity.schedule.ListOfGroupsEntity


/**
 * ### ListOfGroupsModel - элемент списка всех групп, отображаемых в списке групп в добавлении расписания
 * * __course__ (_2_) - номер курса (может быть 0, тогда группу не отображаем
 * * __calendarId__ (_4t4b...eq0@group.calendar.google.com_) - строка, которая добавляется к ссылке
 * `https://calendar.google.com/calendar/u/0/r?cid=`
 * * __facultyAbbrev__ (_ФКП_) - факультет, к которому относится группа
 * * __name__ (_210101_) - номер гурппы
 * * __specialityAbbrev__ (_ИСиТ(в ОПБ)_) - аббревиатура специальности
 * * __specialityName__ (_Информационные системы..._) - полное название специальности
 */
data class ListOfGroupsModel(
    val course: Int, // 3
    val calendarId: String, // 4t4b9qurekmm2nnb2tjjdcseq0@group.calendar.google.com
    val facultyAbbrev: String, // ФКП
    val name: String, // 010101
    val specialityAbbrev: String, // ИСиТ(в ОПБ)
    val specialityName: String // Информационные системы и технологии (по направлениям)
) {
    fun toEntity(): ListOfGroupsEntity = ListOfGroupsEntity(
        this.course,
        this.calendarId,
        this.facultyAbbrev,
        this.name,
        this.specialityAbbrev,
        this.specialityName
    )
}
