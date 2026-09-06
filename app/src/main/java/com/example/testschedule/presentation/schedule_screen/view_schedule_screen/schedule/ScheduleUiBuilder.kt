package com.example.testschedule.presentation.schedule_screen.view_schedule_screen.schedule

import com.example.testschedule.domain.model.schedule.ScheduleModel
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.spec_items.getLessonByDate
import java.util.Calendar
import java.util.GregorianCalendar

enum class ScheduleViewMode {
    BY_DAYS,
    BY_WEEKS,
    EXAMS
}

enum class SubgroupFilter(val number: Int) {
    ALL(0),
    FIRST(1),
    SECOND(2)
}

data class ScheduleDayDisplay(
    val date: Long?,
    val dayOfWeek: Int,
    val week: Int?,
    val lessons: List<ScheduleModel.WeeksSchedule.Lesson>,
    val isRestDay: Boolean = false
)

object ScheduleUiBuilder {

    fun buildByDays(
        schedule: ScheduleModel,
        currentWeek: Int,
        lastWeekUpdate: Long,
        subgroup: SubgroupFilter
    ): List<ScheduleDayDisplay> {
        val today = Calendar.getInstance().atStartOfDay()
        val result = mutableListOf<ScheduleDayDisplay>()
        val exams = schedule.exams.orEmpty().toSet()
        val lastDatedEvent = schedule.schedules
            .asSequence()
            .flatMap { it.allLessons() }
            .filterNot { it in exams }
            .mapNotNull { it.dateLesson }
            .maxOrNull()
        val endDate = listOfNotNull(schedule.endLessonsDate, lastDatedEvent).maxOrNull()

        if (endDate != null) {
            val startDate = maxOf(today.timeInMillis, schedule.startLessonsDate ?: today.timeInMillis)
            val current = GregorianCalendar().apply { timeInMillis = startDate }.atStartOfDay()

            while (current.timeInMillis <= endDate) {
                if (current.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    val day = getLessonByDate(
                        cal = current,
                        lastUpdate = lastWeekUpdate,
                        week = currentWeek,
                        all = schedule.schedules
                    )
                    val lessons = day.lessons
                        .filterNot { it in exams }
                        .filterFor(subgroup)
                    result += ScheduleDayDisplay(
                        date = day.date,
                        dayOfWeek = day.dayOfWeek,
                        week = day.week,
                        lessons = lessons,
                        isRestDay = lessons.isEmpty()
                    )
                }
                current.add(Calendar.DAY_OF_MONTH, 1)
            }
        } else {
            result += buildByWeeks(schedule, subgroup, currentWeek, lastWeekUpdate)
        }

        val examDays = buildExams(schedule, subgroup, includePast = false)
        if (endDate == null) return result + examDays

        return (result + examDays)
            .groupBy { it.date }
            .values
            .map { sameDay ->
                sameDay.first().copy(
                    lessons = sameDay.flatMap { it.lessons }.distinct().sortedBy { it.startLessonTime },
                    isRestDay = sameDay.all { it.lessons.isEmpty() }
                )
            }
            .sortedBy { it.date }
    }

    fun buildByWeeks(
        schedule: ScheduleModel,
        subgroup: SubgroupFilter,
        currentWeek: Int,
        lastWeekUpdate: Long
    ): List<ScheduleDayDisplay> = buildList {
        val exams = schedule.exams.orEmpty().toSet()
        schedule.schedules.forEachIndexed { weekIndex, week ->
            listOf(
                Calendar.MONDAY to week.monday,
                Calendar.TUESDAY to week.tuesday,
                Calendar.WEDNESDAY to week.wednesday,
                Calendar.THURSDAY to week.thursday,
                Calendar.FRIDAY to week.friday,
                Calendar.SATURDAY to week.saturday
            ).forEach { (dayOfWeek, dayLessons) ->
                val lessons = dayLessons
                    .filterNot { it in exams }
                    .filter { lesson ->
                        lesson.dateLesson == null ||
                            academicWeek(lesson.dateLesson, currentWeek, lastWeekUpdate) == weekIndex + 1
                    }
                    .filterFor(subgroup)
                if (lessons.isNotEmpty()) {
                    add(
                        ScheduleDayDisplay(
                            date = null,
                            dayOfWeek = dayOfWeek,
                            week = weekIndex + 1,
                            lessons = lessons
                        )
                    )
                }
            }
        }
    }

    fun buildExams(
        schedule: ScheduleModel,
        subgroup: SubgroupFilter,
        includePast: Boolean = true
    ): List<ScheduleDayDisplay> {
        val today = Calendar.getInstance().atStartOfDay().timeInMillis
        return schedule.exams.orEmpty()
            .filterFor(subgroup)
            .filter { it.dateLesson != null }
            .filter { includePast || (it.dateLesson ?: 0L) >= today }
            .groupBy { it.dateLesson ?: 0L }
            .toSortedMap()
            .map { (date, lessons) ->
                val calendar = Calendar.getInstance().apply { timeInMillis = date }
                ScheduleDayDisplay(
                    date = date,
                    dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK),
                    week = null,
                    lessons = lessons.sortedBy { it.startLessonTime }
                )
            }
    }

    private fun List<ScheduleModel.WeeksSchedule.Lesson>.filterFor(
        subgroup: SubgroupFilter
    ): List<ScheduleModel.WeeksSchedule.Lesson> = filter { lesson ->
        subgroup == SubgroupFilter.ALL || lesson.numSubgroup == 0 || lesson.numSubgroup == subgroup.number
    }

    private fun ScheduleModel.WeeksSchedule.allLessons() = sequenceOf(
        monday,
        tuesday,
        wednesday,
        thursday,
        friday,
        saturday
    ).flatten()

    private fun academicWeek(date: Long, referenceWeek: Int, referenceMonday: Long): Int {
        val calendar = Calendar.getInstance().apply { timeInMillis = date }.atStartOfDay()
        val daysFromMonday = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> 6
            else -> calendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY
        }
        calendar.add(Calendar.DAY_OF_MONTH, -daysFromMonday)
        val weekOffset = ((calendar.timeInMillis - referenceMonday) / (7 * 24 * 60 * 60 * 1000L)).toInt()
        return Math.floorMod(referenceWeek - 1 + weekOffset, 4) + 1
    }

    private fun Calendar.atStartOfDay(): GregorianCalendar = GregorianCalendar(
        get(Calendar.YEAR),
        get(Calendar.MONTH),
        get(Calendar.DAY_OF_MONTH),
        0,
        0,
        0
    ).apply { set(Calendar.MILLISECOND, 0) }
}
