package com.example.testschedule.data.remote.dto.schedule_view

import com.google.gson.annotations.SerializedName

data class ScheduleDto(
    val startDate: String?,
    val endDate: String?,
    val startExamsDate: String?,
    val endExamsDate: String?,
    val employeeDto: EmployeeDto?,
    val studentGroupDto : StudentGroupDto?,
    val schedules : SchedulesDto?,
    val exams: List<SchedulesDto.SchedulesItemDto>?
) {
    data class EmployeeDto(
        val id: Int,
        val firstName: String,
        val middleName: String?,
        val lastName: String,
        val photoLink: String?,
        val degree: String?,
        val degreeAbbrev: String?,
        val rank: String?,
        val email: String?,
        val urlId: String,
        val calendarId: String?,
        val jobPositions: Any? // TODO
    )

    data class StudentGroupDto(
        val name: String,
        val facultyId: Int,
        val facultyAbbrev: String,
        val specialityDepartmentEducationFormId: Int,
        val specialityName: String,
        val specialityAbbrev: String,
        val course: Int,
        val id: Int,
        val calendarId: String?,
        val educationDegree: Int,
    )

    data class SchedulesDto(
        @SerializedName("Понедельник")
        val monday : List<SchedulesItemDto>?,
        @SerializedName("Вторник")
        val tuesday : List<SchedulesItemDto>?,
        @SerializedName("Среда")
        val wednesday : List<SchedulesItemDto>?,
        @SerializedName("Четверг")
        val thursday : List<SchedulesItemDto>?,
        @SerializedName("Пятница")
        val friday : List<SchedulesItemDto>?,
        @SerializedName("Суббота")
        val saturday : List<SchedulesItemDto>?,
    ) {
        data class SchedulesItemDto(
            val auditories: List<String>,
            val endLessonTime: String,
            val lessonTypeAbbrev: String?,
            val note: String?,
            val numSubgroup: Int,
            val startLessonTime: String,
            val studentGroups: List<StudentGroupsItemDto>,
            val subject: String?,
            val subjectFullName: String?,
            val weekNumber : List<Int>?,
            val employees: List<EmployeeDto>?,
            val dateLesson: String?,
            val startLessonDate: String?,
            val endLessonDate: String?,
            val announcement: Boolean,
            val split: Boolean
        ) {
            data class StudentGroupsItemDto(
                val specialityName: String,
                val specialityCode: String,
                val numberOfStudents: Int,
                val name: String,
                val educationDegree: Int,
            )
        }
    }
}