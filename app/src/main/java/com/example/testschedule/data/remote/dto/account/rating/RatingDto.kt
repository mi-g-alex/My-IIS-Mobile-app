package com.example.testschedule.data.remote.dto.account.rating

import com.example.testschedule.domain.model.account.rating.RatingModel
import com.google.gson.annotations.SerializedName

data class RatingDto(
    @SerializedName("subjects") val subjects: List<SubjectDto> = emptyList(),
    @SerializedName("deadlines") val deadlines: List<DeadlineGroupDto> = emptyList(),
    @SerializedName("percentageMarks") val percentageMarks: List<PercentageMarkDto> = emptyList()
) {
    data class SubjectDto(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("abbrev") val abbrev: String,
        @SerializedName("lessonTypes") val lessonTypes: List<LessonTypeDto> = emptyList()
    )

    data class LessonTypeDto(
        @SerializedName("id") val id: Int,
        @SerializedName("abbrev") val abbrev: String,
        @SerializedName("termHoursId") val termHoursId: Int,
        @SerializedName("lessons") val lessons: List<LessonDto> = emptyList()
    )

    data class LessonDto(
        @SerializedName("id") val id: Int,
        @SerializedName("controlPoint") val controlPoint: String,
        @SerializedName("dateString") val dateString: String,
        @SerializedName("gradebookOmissions") val gradebookOmissions: Int,
        @SerializedName("marks") val marks: List<MarkDto> = emptyList(),
        @SerializedName("subGroup") val subGroup: Int?
    )

    data class MarkDto(
        @SerializedName("mark") val mark: Int?,
        @SerializedName("taskNumber") val taskNumber: Int?
    )

    data class DeadlineGroupDto(
        @SerializedName("termHoursId") val termHoursId: Int,
        @SerializedName("labCount") val labCount: Int?,
        @SerializedName("deadlines") val deadlines: List<DeadlineDto> = emptyList()
    )

    data class DeadlineDto(
        @SerializedName("lessonId") val lessonId: Int,
        @SerializedName("taskNumber") val taskNumber: Int?
    )

    data class PercentageMarkDto(
        @SerializedName("discipline") val discipline: String,
        @SerializedName("date") val date: String,
        @SerializedName("number") val number: Double?
    )

    fun toModel(disrespectfulOmissions: List<DisrespectfulOmissionDto>): RatingModel {
        val subjectNames = subjects.associate { it.abbrev to it.name }
        val lessonsById = subjects
            .flatMap { it.lessonTypes }
            .flatMap { it.lessons }
            .associateBy { it.id }
        val disrespectfulHoursByKey = disrespectfulOmissions
            .groupingBy { omission ->
                OmissionKey(
                    subjectId = omission.subject.id,
                    lessonType = omission.lessonTypeAbbrev,
                    date = omission.date.normalizedDate()
                )
            }
            .fold(0) { hours, omission -> hours + omission.hours }
        val disrespectfulHoursByLessonId = buildMap {
            subjects.forEach { subject ->
                subject.lessonTypes.forEach { lessonType ->
                    lessonType.lessons
                        .groupBy { lesson -> lesson.dateString.normalizedDate() }
                        .forEach { (date, lessonsOnDate) ->
                            val hours = disrespectfulHoursByKey[
                                OmissionKey(subject.id, lessonType.abbrev, date)
                            ] ?: 0
                            if (hours > 0) {
                                val targetLesson = lessonsOnDate.firstOrNull {
                                    it.gradebookOmissions > 0
                                } ?: lessonsOnDate.first()
                                put(targetLesson.id, hours)
                            }
                        }
                }
            }
        }

        val deadlineInfoBySubject = subjects.mapNotNull { subject ->
            val labLessonType = subject.lessonTypes.firstOrNull { it.id == LAB_LESSON_TYPE_ID }
                ?: return@mapNotNull null
            if (labLessonType.lessons.isEmpty()) return@mapNotNull null

            val deadlineGroup = deadlines.firstOrNull {
                it.termHoursId == labLessonType.termHoursId
            }
            if (deadlineGroup?.labCount == 0) return@mapNotNull null

            val submittedLabs = labLessonType.lessons.flatMap { lesson ->
                lesson.marks.mapNotNull { mark ->
                    mark.taskNumber
                        ?.takeIf { mark.mark != null }
                        ?.let { taskNumber -> lesson.id to taskNumber }
                }
            }.toSet().size

            val subjectDeadlines = deadlineGroup?.deadlines.orEmpty().mapNotNull { deadline ->
                lessonsById[deadline.lessonId]?.let { lesson ->
                    RatingModel.Point.LessonByName.DeadlineInfo.Deadline(
                        date = lesson.dateString,
                        taskNumber = deadline.taskNumber
                    )
                }
            }

            subject.abbrev to RatingModel.Point.LessonByName.DeadlineInfo(
                totalLabs = deadlineGroup?.labCount,
                submittedLabs = submittedLabs,
                deadlines = subjectDeadlines
            )
        }.toMap()

        val percentageMarksBySubject = percentageMarks.groupBy { it.discipline }.mapValues { (_, marks) ->
            marks.mapNotNull { mark ->
                mark.number?.let { number ->
                    RatingModel.Point.LessonByName.PercentageMark(
                        date = mark.date,
                        number = number.toDisplayString()
                    )
                }
            }
        }

        val pointData = mutableMapOf<
            String,
            MutableMap<
                String,
                MutableMap<
                    String,
                    MutableList<RatingModel.Point.LessonByName.LessonsByType.Lesson>
                    >
                >
            >()
        pointData[ALL_POINTS] = mutableMapOf()

        fun addLesson(
            point: String,
            subject: SubjectDto,
            lessonType: LessonTypeDto,
            lesson: LessonDto
        ) {
            val lessons = pointData
                .getOrPut(point) { mutableMapOf() }
                .getOrPut(subject.abbrev) { mutableMapOf() }
                .getOrPut(lessonType.abbrev) { mutableListOf() }

            lessons += RatingModel.Point.LessonByName.LessonsByType.Lesson(
                name = "",
                point = lesson.controlPoint,
                date = lesson.dateString,
                omissions = disrespectfulHoursByLessonId[lesson.id] ?: 0,
                marks = lesson.marks.mapNotNull { it.mark }
            )
        }

        subjects.forEach { subject ->
            val summaryTypes = pointData
                .getValue(ALL_POINTS)
                .getOrPut(subject.abbrev) { mutableMapOf() }

            subject.lessonTypes.forEach { lessonType ->
                summaryTypes.getOrPut(lessonType.abbrev) { mutableListOf() }

                lessonType.lessons.forEach { lesson ->
                    if (lesson.controlPoint.isNotBlank()) {
                        addLesson(lesson.controlPoint, subject, lessonType, lesson)
                    }
                    addLesson(ALL_POINTS, subject, lessonType, lesson)
                }
            }
        }

        val points = pointData.mapValues { (pointName, subjectsByName) ->
            val subjectModels = subjectsByName.mapValues { (subjectAbbrev, lessonsByType) ->
                val typeModels = lessonsByType.mapValues { (_, lessons) ->
                    RatingModel.Point.LessonByName.LessonsByType(
                        all = lessons,
                        countOfMarks = lessons.flatMap { it.marks },
                        countOfOmissions = lessons.sumOf { it.omissions }
                    )
                }

                RatingModel.Point.LessonByName(
                    types = typeModels,
                    name = subjectNames[subjectAbbrev].orEmpty(),
                    allTypes = typeModels.keys,
                    listOfMarks = typeModels.values.flatMap { it.countOfMarks },
                    countOfOmissions = typeModels.values.sumOf { it.countOfOmissions },
                    deadlineInfo = deadlineInfoBySubject[subjectAbbrev]
                        .takeIf { pointName == ALL_POINTS },
                    percentageMarks = percentageMarksBySubject[subjectAbbrev]
                        .orEmpty()
                        .takeIf { pointName == ALL_POINTS }
                        .orEmpty()
                )
            }

            RatingModel.Point(
                subjects = subjectModels,
                listOfSubjects = subjectModels.keys,
                listOfMarks = subjectModels.values.flatMap { it.listOfMarks },
                countOfOmissions = subjectModels.values.sumOf { it.countOfOmissions }
            )
        }

        return RatingModel(
            points = points,
            allPoints = points.keys - ALL_POINTS
        )
    }

    private fun Double.toDisplayString(): String =
        if (this % 1.0 == 0.0) toInt().toString() else toString()

    private fun String.normalizedDate(): String {
        val parts = split('.')
        return if (parts.size == 3) {
            "${parts[2]}-${parts[1]}-${parts[0]}"
        } else {
            this
        }
    }

    private data class OmissionKey(
        val subjectId: Int,
        val lessonType: String,
        val date: String
    )

    private companion object {
        const val ALL_POINTS = "all_points"
        const val LAB_LESSON_TYPE_ID = 4
    }
}
