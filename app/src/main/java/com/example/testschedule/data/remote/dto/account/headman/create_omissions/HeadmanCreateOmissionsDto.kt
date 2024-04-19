package com.example.testschedule.data.remote.dto.account.headman.create_omissions

data class HeadmanCreateOmissionsDto(
    val idLesson: Int, // 1031079
    val studentOmissionsHoursDtoList: List<StudentOmissionsHoursDto>
) {
    data class StudentOmissionsHoursDto(
        val hours: Int, // 4
        val student: Int // 543551
    )
}