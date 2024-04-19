package com.example.testschedule.data.remote.dto.account.headman.create_omissions

import com.example.testschedule.domain.model.account.headman.create_omissions.HeadmanGetOmissionsModel

data class HeadmanGetOmissionsDto(
    val lessons: List<LessonDto>
) {
    data class LessonDto(
        val dateString: String, // 03-04-2024
        val id: Int, // 969064
        val lessonPeriod: LessonPeriodDto,
        val lessonTypeAbbrev: String, // ЛР
        val nameAbbrev: String, // ИГИ
        val students: List<StudentDto>,
        val subGroup: Int // 0 | 1 | 2
    ) {
        data class LessonPeriodDto(
            val endTime: String, // 11:55
            val lessonPeriodHours: Int, // 2
            val startTime: String // 10:35
        ) {
            fun toModel() = HeadmanGetOmissionsModel.LessonModel.LessonPeriodModel(
                startTime = startTime,
                endTime = endTime,
                lessonPeriodHours = lessonPeriodHours
            )
        }


        data class StudentDto(
            val fio: String, // Артиш Виктория Олеговна
            val id: Int, // 543535
            val omission: OmissionDto?
        ) {
            data class OmissionDto(
                val id: Int, // 1080408
                val missedHours: Int, // 2
                val respectfulOmission: Boolean // false
            ) {
                fun toModel() = HeadmanGetOmissionsModel.LessonModel.StudentModel.OmissionModel(
                    id = id,
                    missedHours = missedHours,
                    respectfulOmission = respectfulOmission
                )
            }

            fun toModel() = HeadmanGetOmissionsModel.LessonModel.StudentModel(
                id = id,
                fio = fio,
                omission = omission?.toModel()
            )
        }

        fun toModel() = HeadmanGetOmissionsModel.LessonModel(
            id = id,
            dateString = dateString,
            nameAbbrev = nameAbbrev,
            lessonTypeAbbrev = lessonTypeAbbrev,
            subGroup = subGroup,
            lessonPeriod = lessonPeriod.toModel(),
            students = students.map { it.toModel() }

        )
    }

    fun toModel(date: String) = HeadmanGetOmissionsModel(
        lessons = lessons.map { it.toModel() },
        date = date
    )
}