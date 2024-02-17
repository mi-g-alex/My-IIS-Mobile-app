package com.example.testschedule.data.remote.dto.account.rating

import com.example.testschedule.domain.model.account.rating.RatingModel

data class RatingDto(
    val student: StudentDto,
    val students: List<StudentDto>
) {
    data class StudentDto(
        val fio: String, // Иванов Иван Иванович
        val id: Int?, // 123456
        val lessons: List<LessonDto>,
        val subGroup: Int, // 0
        val subGroupStudent: Int // 2
    ) {
        data class LessonDto(
            val controlPoint: String, // 01.10.2023
            val dateString: String, // 01.09.2023
            val gradeBookOmissions: Int, // 0
            val id: Int, // 911022
            val lessonNameAbbrev: String, // ФизК
            val lessonTypeAbbrev: String, // ПЗ
            val lessonTypeId: Int, // 3
            val marks: List<Int>,
            val subGroup: Int // 0
        )
    }

    fun toModel(): RatingModel {
        val les = this.student.lessons

        data class Lesson(
            val name: String,
            val point: String,
            val date: String,
            val hours: Int,
            val marks: List<Int>
        ) {
            fun toModel() = RatingModel.Point.LessonByName.LessonsByType.Lesson(
                name = this.name,
                point = this.point,
                date = this.date,
                omissions = this.hours,
                marks = this.marks.toList()
            )
        }

        data class LessonsByType(
            val all: MutableList<Lesson> = mutableListOf(),
            val countOfMarks: MutableList<Int> = mutableListOf(),
            var countOfOmissions: Int = 0
        ) {
            fun toModel() = RatingModel.Point.LessonByName.LessonsByType(
                all = this.all.map { it.toModel() }.toList(),
                countOfMarks = this.countOfMarks.toList(),
                countOfOmissions = this.countOfOmissions
            )
        }

        data class LessonByName(
            val types: MutableMap<String, LessonsByType> = mutableMapOf(),
            val allTypes: MutableSet<String> = mutableSetOf(),
            val listOfMarks: MutableList<Int> = mutableListOf(),
            var countOfOmissions: Int = 0
        ) {
            fun toModel(name: String) = RatingModel.Point.LessonByName(
                types = this.types.mapValues { it.value.toModel() },
                allTypes = this.allTypes.toSet(),
                listOfMarks = this.listOfMarks.toList(),
                name = name,
                countOfOmissions = this.countOfOmissions
            )
        }

        data class Point(
            val subjects: MutableMap<String, LessonByName> = mutableMapOf(),
            val listOfSubjects: MutableSet<String> = mutableSetOf(),
            val listOfMarks: MutableList<Int> = mutableListOf(),
            var countOfOmissions: Int = 0
        ) {
            fun toModel() = RatingModel.Point(
                subjects = this.subjects.mapValues { it.value.toModel(it.key) },
                listOfSubjects = this.listOfSubjects.toSet(),
                listOfMarks = this.listOfMarks.toList(),
                countOfOmissions = this.countOfOmissions
            )
        }

        val points: MutableMap<String, Point> = mutableMapOf()
        val allPoints: MutableSet<String> = mutableSetOf()
        allPoints.add("all_points")
        les.forEach {
            allPoints.add(it.controlPoint)
            if (!points.containsKey(it.controlPoint)) points[it.controlPoint] = Point()

            points[it.controlPoint]?.countOfOmissions =
                points[it.controlPoint]?.countOfOmissions!! + it.gradeBookOmissions
            points[it.controlPoint]?.listOfMarks?.addAll(it.marks)
            points[it.controlPoint]?.listOfSubjects?.add(it.lessonNameAbbrev)

            if (points[it.controlPoint]?.subjects?.containsKey(it.lessonNameAbbrev) != true)
                points[it.controlPoint]?.subjects?.set(it.lessonNameAbbrev, LessonByName())

            points[it.controlPoint]?.subjects?.get(it.lessonNameAbbrev)?.allTypes?.add(it.lessonTypeAbbrev)
            points[it.controlPoint]?.subjects?.get(it.lessonNameAbbrev)?.listOfMarks?.addAll(it.marks)
            points[it.controlPoint]?.subjects?.get(it.lessonNameAbbrev)?.countOfOmissions =
                points[it.controlPoint]?.subjects?.get(it.lessonNameAbbrev)?.countOfOmissions!! + it.gradeBookOmissions

            if (points[it.controlPoint]?.subjects?.get(it.lessonNameAbbrev)?.types?.containsKey(it.lessonTypeAbbrev) != true)
                points[it.controlPoint]?.subjects?.get(it.lessonNameAbbrev)?.types?.set(
                    it.lessonTypeAbbrev,
                    LessonsByType()
                )

            points[it.controlPoint]?.subjects?.get(it.lessonNameAbbrev)?.types?.get(it.lessonTypeAbbrev)?.all?.add(
                Lesson(
                    name = it.lessonNameAbbrev,
                    point = it.controlPoint,
                    date = it.dateString,
                    hours = it.gradeBookOmissions,
                    marks = it.marks
                )
            )

            points[it.controlPoint]?.subjects?.get(it.lessonNameAbbrev)?.types?.get(it.lessonTypeAbbrev)?.countOfMarks?.addAll(
                it.marks
            )
            points[it.controlPoint]?.subjects?.get(it.lessonNameAbbrev)?.types?.get(it.lessonTypeAbbrev)?.countOfOmissions =
                points[it.controlPoint]?.subjects?.get(it.lessonNameAbbrev)?.types?.get(it.lessonTypeAbbrev)?.countOfOmissions!! + it.gradeBookOmissions



            if (!points.containsKey("all_points")) points["all_points"] = Point()

            points["all_points"]?.countOfOmissions =
                points["all_points"]?.countOfOmissions!! + it.gradeBookOmissions
            points["all_points"]?.listOfMarks?.addAll(it.marks)
            points["all_points"]?.listOfSubjects?.add(it.lessonNameAbbrev)

            if (points["all_points"]?.subjects?.containsKey(it.lessonNameAbbrev) != true)
                points["all_points"]?.subjects?.set(it.lessonNameAbbrev, LessonByName())

            points["all_points"]?.subjects?.get(it.lessonNameAbbrev)?.allTypes?.add(it.lessonTypeAbbrev)
            points["all_points"]?.subjects?.get(it.lessonNameAbbrev)?.listOfMarks?.addAll(it.marks)
            points["all_points"]?.subjects?.get(it.lessonNameAbbrev)?.countOfOmissions =
                points["all_points"]?.subjects?.get(it.lessonNameAbbrev)?.countOfOmissions!! + it.gradeBookOmissions

            if (points["all_points"]?.subjects?.get(it.lessonNameAbbrev)?.types?.containsKey(it.lessonTypeAbbrev) != true)
                points["all_points"]?.subjects?.get(it.lessonNameAbbrev)?.types?.set(
                    it.lessonTypeAbbrev,
                    LessonsByType()
                )

            points["all_points"]?.subjects?.get(it.lessonNameAbbrev)?.types?.get(it.lessonTypeAbbrev)?.all?.add(
                Lesson(
                    name = it.lessonNameAbbrev,
                    point = it.controlPoint,
                    date = it.dateString,
                    hours = it.gradeBookOmissions,
                    marks = it.marks
                )
            )

            points["all_points"]?.subjects?.get(it.lessonNameAbbrev)?.types?.get(it.lessonTypeAbbrev)?.countOfMarks?.addAll(
                it.marks
            )
            points["all_points"]?.subjects?.get(it.lessonNameAbbrev)?.types?.get(it.lessonTypeAbbrev)?.countOfOmissions =
                points["all_points"]?.subjects?.get(it.lessonNameAbbrev)?.types?.get(it.lessonTypeAbbrev)?.countOfOmissions!! + it.gradeBookOmissions
        }


        return RatingModel(
            points = points.mapValues { it.value.toModel() },
            allPoints = allPoints.toSet()
        )
    }
}