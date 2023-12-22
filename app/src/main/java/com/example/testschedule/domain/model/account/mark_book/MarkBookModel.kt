package com.example.testschedule.domain.model.account.mark_book

import com.example.testschedule.data.local.entity.mark_book.MarkBookEntity

data class MarkBookModel(
    val averageMark: Double, // 9
    val semesters: Map<Int, Semester>,
    val number: String // 25350033
) {
    data class Semester(
        val averageMark: Double, // 9.17
        val marks: List<Mark>
    ) {
        data class Mark(
            val commonMark: Double?, // 8.14569536423841 // Средняя оценка по предмету за 4 года
            val commonRetakes: Double?, // 0.006622516556291391 // шанс отлететь на пересдачу
            val credits: Int, // 3
            val date: String, // 24.12.2022 // Дата проведения экзамена
            val formOfControl: String, // Диф.зачет // Тип экзамаена
            val fullSubject: String, // Логика //
            val hours: String, // 108.0 // Кол-во часов
            val mark: String, // 7 // Оценка за экзамен / зач ...
            val retakesCount: Int, // 0 // Кол-во твоих пересдач
            val subject: String, // Логика // Кракткое название предмета (ОАиП например)
            val teacher: String // Бархатков А. И. // ФИО преподавателя
        ) {
            fun toEntity() = MarkBookEntity.Semester.Mark(
                commonMark = this.commonMark,
                commonRetakes = this.commonRetakes,
                credits = this.credits,
                date = this.date,
                formOfControl = this.formOfControl,
                fullSubject = this.fullSubject,
                hours = this.hours,
                mark = this.mark,
                retakesCount = this.retakesCount,
                subject = this.subject,
                teacher = this.teacher
            )
        }

        fun toEntity() = MarkBookEntity.Semester(
            averageMark = this.averageMark,
            marks = this.marks.map { it.toEntity() }
        )
    }

    fun toEntity() = MarkBookEntity(
        key = 0,
        averageMark = this.averageMark,
        semesters = this.semesters.mapValues { it.value.toEntity() },
        number = this.number
    )
}