package com.example.testschedule.data.repository

import com.example.testschedule.domain.model.account.headman.create_omissions.HeadmanGetOmissionsModel
import com.example.testschedule.domain.model.schedule.ScheduleModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

internal fun orderHeadmanLessonsBySchedule(
    lessons: List<HeadmanGetOmissionsModel.LessonModel>,
    date: String,
    schedule: ScheduleModel?,
    currentWeek: Int?,
    referenceDateMillis: Long = System.currentTimeMillis()
): List<HeadmanGetOmissionsModel.LessonModel> {
    val selectedDateMillis = parseDate(date)
    if (schedule == null || currentWeek == null || currentWeek !in 1..4 || selectedDateMillis == null) {
        return lessons.sortedWith(headmanLessonComparator())
    }

    val academicWeek = academicWeek(
        dateMillis = selectedDateMillis,
        referenceWeek = currentWeek,
        referenceDateMillis = referenceDateMillis
    )
    val scheduleLessons = schedule.schedules
        .getOrNull(academicWeek - 1)
        ?.lessonsFor(selectedDateMillis)
        .orEmpty()
        .filter { it.isActiveOn(selectedDateMillis) }
        .sortedBy { it.startLessonTime }
        .toMutableList()

    val enrichedLessons = lessons.map { lesson ->
        val exactMatch = scheduleLessons.indexOfFirst {
            it.matches(lesson, matchSubgroupExactly = true)
        }
        val compatibleMatch = scheduleLessons.indexOfFirst {
            it.matches(lesson, matchSubgroupExactly = false)
        }
        val matchIndex = exactMatch.takeIf { it >= 0 } ?: compatibleMatch

        if (matchIndex < 0) {
            lesson
        } else {
            val scheduleLesson = scheduleLessons.removeAt(matchIndex)
            lesson.copy(
                lessonPeriod = lesson.lessonPeriod.copy(
                    startTime = scheduleLesson.startLessonTime.toTimeString(),
                    endTime = scheduleLesson.endLessonTime.toTimeString()
                ),
                scheduleInfo = HeadmanGetOmissionsModel.LessonModel.ScheduleInfoModel(
                    subjectFullName = scheduleLesson.subjectFullName,
                    subjectAbbrev = scheduleLesson.subject,
                    auditories = scheduleLesson.auditories.filter { it.isNotBlank() }.distinct(),
                    teacherFullNames = scheduleLesson.employees.orEmpty()
                        .map { employee ->
                            listOfNotNull(
                                employee.lastName,
                                employee.firstName,
                                employee.middleName?.takeIf { it.isNotBlank() }
                            ).joinToString(" ")
                        }
                        .filter { it.isNotBlank() }
                        .distinct()
                )
            )
        }
    }

    return enrichedLessons.sortedWith(headmanLessonComparator())
}

private fun headmanLessonComparator() = compareBy<HeadmanGetOmissionsModel.LessonModel>(
    { it.lessonPeriod.startTime.toMinutes() ?: Int.MAX_VALUE },
    { it.nameAbbrev.lowercase(Locale.ROOT) },
    { it.lessonTypeAbbrev.lowercase(Locale.ROOT) },
    { it.subGroup }
)

private fun ScheduleModel.WeeksSchedule.lessonsFor(dateMillis: Long) =
    when (Calendar.getInstance().apply { timeInMillis = dateMillis }.get(Calendar.DAY_OF_WEEK)) {
        Calendar.MONDAY -> monday
        Calendar.TUESDAY -> tuesday
        Calendar.WEDNESDAY -> wednesday
        Calendar.THURSDAY -> thursday
        Calendar.FRIDAY -> friday
        Calendar.SATURDAY -> saturday
        else -> emptyList()
    }

private fun ScheduleModel.WeeksSchedule.Lesson.matches(
    lesson: HeadmanGetOmissionsModel.LessonModel,
    matchSubgroupExactly: Boolean
): Boolean {
    val headmanSubject = lesson.nameAbbrev.normalizedLessonLabel()
    val subjectMatches = headmanSubject == subject.normalizedLessonLabel() ||
        headmanSubject == subjectFullName.normalizedLessonLabel()
    val typeMatches = lesson.lessonTypeAbbrev.normalizedLessonLabel() ==
        lessonTypeAbbrev.normalizedLessonLabel()
    val subgroupMatches = if (matchSubgroupExactly) {
        lesson.subGroup == numSubgroup
    } else {
        lesson.subGroup == 0 || numSubgroup == 0
    }

    return subjectMatches && typeMatches && subgroupMatches
}

private fun ScheduleModel.WeeksSchedule.Lesson.isActiveOn(dateMillis: Long): Boolean {
    dateLesson?.let { return it.atStartOfDay() == dateMillis.atStartOfDay() }

    val afterStart = startLessonDate == null || dateMillis.atStartOfDay() >= startLessonDate.atStartOfDay()
    val beforeEnd = endLessonDate == null || dateMillis.atStartOfDay() <= endLessonDate.atStartOfDay()
    return afterStart && beforeEnd
}

private fun academicWeek(
    dateMillis: Long,
    referenceWeek: Int,
    referenceDateMillis: Long
): Int {
    val selectedMonday = dateMillis.startOfWeek()
    val referenceMonday = referenceDateMillis.startOfWeek()
    val weekOffset = ((selectedMonday - referenceMonday) / MILLIS_IN_WEEK).toInt()
    return Math.floorMod(referenceWeek - 1 + weekOffset, 4) + 1
}

private fun parseDate(date: String): Long? = runCatching {
    SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).apply {
        isLenient = false
    }.parse(date)?.time
}.getOrNull()

private fun String.normalizedLessonLabel(): String =
    trim().lowercase(Locale.ROOT).replace('ё', 'е')

private fun String.toMinutes(): Int? {
    val parts = split(':')
    if (parts.size != 2) return null
    val hours = parts[0].toIntOrNull() ?: return null
    val minutes = parts[1].toIntOrNull() ?: return null
    return hours * 60 + minutes
}

private fun Int.toTimeString(): String =
    String.format(Locale.ROOT, "%02d:%02d", this / 60, this % 60)

private fun Long.startOfWeek(): Long {
    val calendar = Calendar.getInstance().apply { timeInMillis = this@startOfWeek }
    val daysFromMonday = when (calendar.get(Calendar.DAY_OF_WEEK)) {
        Calendar.SUNDAY -> 6
        else -> calendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY
    }
    calendar.add(Calendar.DAY_OF_MONTH, -daysFromMonday)
    return calendar.timeInMillis.atStartOfDay()
}

private fun Long.atStartOfDay(): Long = Calendar.getInstance().run {
    timeInMillis = this@atStartOfDay
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
    timeInMillis
}

private const val MILLIS_IN_WEEK = 7 * 24 * 60 * 60 * 1000L
