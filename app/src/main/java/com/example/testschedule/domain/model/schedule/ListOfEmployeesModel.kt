package com.example.testschedule.domain.model.schedule

import com.example.testschedule.data.local.entity.schedule.ListOfEmployeesEntity

/**
 * ### ListOfEmployeesModel - элемент списка всех преподавателей, отображаемых в списке преподавателей в добавлении расписания
 * * __academicDepartment__ (_["каф. фиб"]_) - список кафедр, к которым он относится
 * * __calendarId__ (_4...q0@group.calendar.google.com_) - строка, которая добавляется к ссылке
 * `https://calendar.google.com/calendar/u/0/r?cid=`
 * * __fio__ (_Абрамов И. И. (профессор)_) - ФИО + ранг
 * * __firstName__ (_Игорь_) - имя
 * * __lastName__ (_Абрамов_) - фамилия
 * * __middleName__ (_ Иванович_) - отчество (может быть NULL потому что да)
 * * __photoLink__ (_https://iis.bsuir.by/api/v1/employees/photo/500434_) - ссылка на фотку. Вообще генерится как `ссылка + id`, поэтому __по ссылке фотки может и не быть)__
 * * __rank__ (_профессор_) - ранг преподавателя
 * * __degree__ (_д.ф.-м.н._) - степень
 * * __urlId__ (_i-abramov_) - string-id
 */

data class ListOfEmployeesModel(
    val academicDepartment: List<String>, // ["каф. фиб"]
    val calendarId: String, // 4t4b9qurekmm2nnb2tjjdcseq0@group.calendar.google.com
    val fio: String, // Абрамов И. И. (профессор)
    val firstName: String, // Игорь
    val lastName: String, // Абрамов
    val middleName: String?, // Иванович
    val photoLink: String?, // https://iis.bsuir.by/api/v1/employees/photo/500434
    val rank: String?, // профессор
    val degree: String?, // д.ф.-м.н.
    val urlId: String // i-abramov
) {
    fun toEntity(): ListOfEmployeesEntity = ListOfEmployeesEntity(
        academicDepartment = this.academicDepartment,
        calendarId = this.calendarId,
        fio = this.fio,
        firstName = this.firstName,
        lastName = this.lastName,
        middleName = this.middleName,
        photoLink = this.photoLink,
        rank = this.rank,
        degree = this.degree,
        urlId = this.urlId
    )
}