package com.example.testschedule.data.remote.dto.account.study.mark_sheet.create

import com.example.testschedule.domain.model.account.study.mark_sheet.create.MarkSheetSubjectsModel

data class SubjectsMarkSheetItemDto(
    val abbrev: String?, // АВС
    val etId: Int?, // 230234
    val lessonTypes: List<LessonTypeDto>?,
    val term: Int? // 4
) {
    data class LessonTypeDto(
        val abbrev: String?, // Зачет
        val focsId: Int?, // 236654
        val isCourseWork: Boolean?, // false
        val isExam: Boolean?, // false
        val isLab: Boolean?, // false
        val isOffset: Boolean?, // true
        val isRemote: Boolean?, // false
        val thId: Int? // 679568
    ) {
        fun toModel() = MarkSheetSubjectsModel.LessonTypeModel(
            abbrev = abbrev ?: "",
            focsId = focsId,
            isCourseWork = isCourseWork ?: false,
            isExam = isExam ?: false,
            isLab = isLab ?: false,
            isOffset = isOffset ?: false,
            isRemote = isRemote ?: false,
            thId = thId
        )
    }

    fun toModel() = MarkSheetSubjectsModel(
        abbrev = abbrev ?: "",
        etId = etId ?: 0,
        lessonTypes = lessonTypes?.map { it.toModel() } ?: emptyList(),
        term = term ?: 0
    )
}