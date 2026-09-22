package com.example.testschedule.domain.model.account.rating


data class RatingModel(
    val points: Map<String, Point>, //
    val allPoints: Set<String> //
) {
    data class Point(
        val subjects: Map<String, LessonByName>,
        val listOfSubjects: Set<String>,
        val listOfMarks: List<Int>,
        val countOfOmissions: Int
    ) {
        data class LessonByName(
            val types: Map<String, LessonsByType>,
            val name: String,
            val allTypes: Set<String>,
            val listOfMarks: List<Int>,
            val countOfOmissions: Int,
            val deadlineInfo: DeadlineInfo? = null,
            val percentageMarks: List<PercentageMark> = emptyList()
        ) {
            data class DeadlineInfo(
                val totalLabs: Int?,
                val submittedLabs: Int,
                val deadlines: List<Deadline>
            ) {
                data class Deadline(
                    val date: String,
                    val taskNumber: Int?
                )
            }

            data class PercentageMark(
                val date: String,
                val number: String
            )

            data class LessonsByType(
                val all: List<Lesson>,
                val countOfMarks: List<Int>,
                val countOfOmissions: Int
            ) {
                data class Lesson(
                    val name: String,
                    val point: String,
                    val date: String,
                    val omissions: Int,
                    val marks: List<Int>
                )
            }
        }
    }
}
