package com.example.testschedule.data.remote.dto

data class ScheduleEmployeeItemDto(
    val academicDepartment: List<String>,
    val calendarId: String, // 4t4b9qurekmm2nnb2tjjdcseq0@group.calendar.google.com
    val degree: String, // д.ф.-м.н.
    val fio: String, // Абрамов И. И. (профессор)
    val firstName: String, // Игорь
    val id: Int, // 500434
    val lastName: String, // Абрамов
    val middleName: String, // Иванович
    val photoLink: String, // https://iis.bsuir.by/api/v1/employees/photo/500434
    val rank: String, // профессор
    val urlId: String // i-abramov
)