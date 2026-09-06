package com.example.testschedule.data.remote.dto.account.headman.create_omissions

import com.example.testschedule.domain.model.account.headman.create_omissions.HeadmanGetOmissionsModel

data class HeadmanGetOmissionsDto(
    val attendance: List<AttendanceDto>,
    val lessons: List<LessonDto>
) {
    data class AttendanceDto(
        val studentId: Int,
        val subGroup: Int?,
        val lessonRecords: Map<String, OmissionDto?>
    ) {
        data class OmissionDto(
            val missedHours: Int,
            val respectfulOmission: Boolean
        ) {
            fun toModel() =
                HeadmanGetOmissionsModel.LessonModel.StudentModel.OmissionModel(
                    id = 0,
                    missedHours = missedHours,
                    respectfulOmission = respectfulOmission
                )
        }
    }

    data class LessonDto(
        val id: Int,
        val date: String,
        val subgroup: Int,
        val numberOfHours: Int
    )

    fun lessonsByDate(
        date: String,
        subjectName: String,
        lessonTypeAbbrev: String,
        studentNames: Map<Int, String>
    ): List<HeadmanGetOmissionsModel.LessonModel> = lessons
        .filter { it.date == date }
        .map { lesson ->
            val students = attendance
                .asSequence()
                .filter { lesson.subgroup == 0 || it.subGroup == lesson.subgroup }
                .map { student ->
                    HeadmanGetOmissionsModel.LessonModel.StudentModel(
                        id = student.studentId,
                        fio = studentNames[student.studentId] ?: student.studentId.toString(),
                        omission = student.lessonRecords[lesson.id.toString()]?.toModel()
                    )
                }
                .sortedBy { it.fio }
                .toList()

            HeadmanGetOmissionsModel.LessonModel(
                id = lesson.id,
                dateString = lesson.date,
                nameAbbrev = subjectName,
                lessonTypeAbbrev = lessonTypeAbbrev,
                subGroup = lesson.subgroup,
                lessonPeriod = HeadmanGetOmissionsModel.LessonModel.LessonPeriodModel(
                    startTime = "",
                    endTime = "",
                    lessonPeriodHours = lesson.numberOfHours
                ),
                students = students
            )
        }
}

data class HeadmanSubjectDto(
    val id: Int,
    val lessonTypeAbbrev: String
)

data class HeadmanStudentDto(
    val studentId: Int,
    val fullName: String
)
