package com.example.testschedule.domain.model.account.headman.create_omissions

data class HeadmanGetOmissionsModel(
    val lessons: List<LessonModel>,
    val date: String,
) {
    data class LessonModel(
        val id: Int, // 969064
        val dateString: String, // 03-04-2024
        val nameAbbrev: String, // ИГИ
        val lessonTypeAbbrev: String, // ЛР
        val subGroup: Int, // 0 | 1 | 2
        val lessonPeriod: LessonPeriodModel,
        val students: List<StudentModel>
    ) {
        data class LessonPeriodModel(
            val startTime: String, // 10:35
            val endTime: String, // 11:55
            val lessonPeriodHours: Int // 2
        )

        data class StudentModel(
            val id: Int, // 543535
            val fio: String, // Артиш Виктория Олеговна
            val omission: OmissionModel?
        ) {
            data class OmissionModel(
                val id: Int, // 1080408
                val missedHours: Int, // 2
                val respectfulOmission: Boolean // false
            )
        }

        fun toStudentModel(omission: StudentModel.OmissionModel?) = StudentLessonOmissionModel(
            id = id,
            nameAbbrev = nameAbbrev,
            lessonTypeAbbrev = lessonTypeAbbrev,
            subGroup = subGroup,
            lessonPeriod = lessonPeriod,
            omission = omission
        )
    }
}