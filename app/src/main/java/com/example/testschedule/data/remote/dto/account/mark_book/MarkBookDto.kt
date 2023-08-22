package com.example.testschedule.data.remote.dto.account.mark_book

import com.example.testschedule.domain.model.account.mark_book.MarkBookModel

data class MarkBookDto(
    val averageMark: Double?, // 9
    val markPages: Map<Int,MapValue>?,
    val number: String // 25350033
) {
    data class MapValue(
        val averageMark: Double?, // 9.17
        val marks: List<MarkDto>?
    ) { // Про нуждные пункты расписано в модели
        data class MarkDto(
            val applicationHasAlreadyBeenSentForAcademicDifferences: Any?, // null
            val applicationHasAlreadyBeenSentForParallel: Any?, // null
            val canLiquidationAcademicDifferences: Any?, // null
            val canStudyInParallel: Any?, // null
            val commonMark: Double?, // 8.14569536423841
            val commonRetakes: Double?, // 0.006622516556291391
            val date: String?, // 24.12.2022
            val formOfControl: String?, // Диф.зачет
            val fullSubject: String?, // Логика
            val hours: String?, // 108.0
            val idFormOfControl: Int?, // 217
            val idSubject: Int?, // 20437
            val lmsEducationTerms: Any?, // null
            val mark: String?, // 7
            val retakesCount: Int?, // 0
            val subject: String?, // Логика
            val teacher: String? // Бархатков А. И.
        ) {
            fun toModel() = MarkBookModel.Semester.Mark(
                commonMark = this.commonMark,
                commonRetakes = this.commonRetakes,
                date = this.date ?: "",
                formOfControl = this.formOfControl ?: "",
                fullSubject = this.fullSubject ?: "",
                hours = this.hours ?: "",
                mark = this.mark ?: "",
                retakesCount = this.retakesCount ?: 0,
                subject = this.subject ?: "",
                teacher = this.teacher ?: ""
            )
        }

        fun toModel() = MarkBookModel.Semester(
            averageMark = this.averageMark ?: 0.0,
            marks = this.marks?.map { it.toModel() } ?: emptyList()
        )
    }

    fun toModel() = MarkBookModel(
        averageMark = this.averageMark ?: 0.0,
        semesters = this.markPages?.mapValues { it.value.toModel() } ?: emptyMap(),
        number = this.number
    )
}