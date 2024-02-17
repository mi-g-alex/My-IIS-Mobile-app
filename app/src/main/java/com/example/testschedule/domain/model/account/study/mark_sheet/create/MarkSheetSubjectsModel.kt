package com.example.testschedule.domain.model.account.study.mark_sheet.create

data class MarkSheetSubjectsModel(
    val abbrev: String, // АВС
    val etId: Int, // 230234
    val lessonTypes: List<LessonTypeModel>,
    val term: Int // 4
) {
    data class LessonTypeModel(
        val abbrev: String, // Зачет
        val focsId: Int?, // 236654
        val isCourseWork: Boolean, // false
        val isExam: Boolean, // false
        val isLab: Boolean, // false
        val isOffset: Boolean, // true
        val isRemote: Boolean, // false
        val thId: Int? // 679568
    )
}