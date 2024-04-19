package com.example.testschedule.domain.model.account.headman.create_omissions

data class StudentLessonOmissionModel(
        val id: Int, // 969064  
        val nameAbbrev: String, // ИГИ
        val lessonTypeAbbrev: String, // ЛР
        val subGroup: Int, // 0 | 1 | 2
        val lessonPeriod: HeadmanGetOmissionsModel.LessonModel.LessonPeriodModel,
        val omission: HeadmanGetOmissionsModel.LessonModel.StudentModel.OmissionModel?
    )