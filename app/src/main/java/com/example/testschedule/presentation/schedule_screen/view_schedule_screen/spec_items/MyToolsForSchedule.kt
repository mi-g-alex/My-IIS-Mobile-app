package com.example.testschedule.presentation.schedule_screen.view_schedule_screen.spec_items

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.snapshotFlow
import com.example.testschedule.domain.model.schedule.ScheduleModel
import java.util.Calendar
import java.util.GregorianCalendar

fun getTimeInString(time: Int): String {
    var s = ""
    val h = time / 60
    val m = time % 60
    s += if (h < 10) "0$h" else h.toString()
    s += ":"
    s += if (m < 10) "0$m" else m.toString()
    return s
}

fun getLessons(
    startDay: GregorianCalendar,
    endDay: Long,
    all: List<ScheduleModel.WeeksSchedule>,
    lastUpdate: Long,
    week: Int
): List<LessonDay> {
    val list = mutableListOf<LessonDay>()
    while (startDay.timeInMillis <= endDay) {
        list.add(
            getLessonByDate(
                startDay, lastUpdate, week, all
            )
        )
        startDay.timeInMillis += 24 * 60 * 60 * 1000
    }
    return list
}

fun getExams(
    all: List<ScheduleModel.WeeksSchedule.Lesson>,
) : List<ExamDay> {
    val list = mutableListOf<ExamDay>()
    all.forEach {it ->
        if(it.dateLesson != null) {
            val cal = GregorianCalendar()
            cal.timeInMillis = it.dateLesson
            list.add(
                ExamDay(
                    date = it.dateLesson,
                    exam = it,
                    month = cal.get(Calendar.MONTH),
                    day = cal.get(Calendar.DAY_OF_MONTH)
                )
            )
        }
    }
    return list.toList()
}

fun getLessonByDate(
    cal: GregorianCalendar,
    lastUpdate: Long,
    week: Int,
    all: List<ScheduleModel.WeeksSchedule>
): LessonDay {
    val currentWeekMonday = GregorianCalendar(
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH),
        0, 0, 0
    )
    if (currentWeekMonday.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
        currentWeekMonday.timeInMillis -= 6 * 24 * 60 * 60 * 1000
    } else {
        currentWeekMonday.timeInMillis -= (cal.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY) * 24 * 60 * 60 * 1000
    }

    var currWeek = (week + ((currentWeekMonday.timeInMillis - lastUpdate) / 1000 / 60 / 60 / 24 / 7).toInt()) % 4
    if (currWeek < 0) currWeek += 4
    if (currWeek == 0) currWeek = 4

    val lessonsTmp: List<ScheduleModel.WeeksSchedule.Lesson> =
        when (cal.get(Calendar.DAY_OF_WEEK)) {
            1 -> emptyList()
            2 -> all[currWeek - 1].monday
            3 -> all[currWeek - 1].tuesday
            4 -> all[currWeek - 1].wednesday
            5 -> all[currWeek - 1].thursday
            6 -> all[currWeek - 1].friday
            7 -> all[currWeek - 1].saturday
            else -> emptyList()
        }
    val lessons = lessonsTmp.filter {
        (((it.startLessonDate ?: 0) <= cal.timeInMillis &&
                (it.endLessonDate ?: 0) >= cal.timeInMillis) ||
                ((it.dateLesson ?: 0) == cal.timeInMillis))
    }
    return LessonDay(
        cal.timeInMillis,
        cal.get(Calendar.DAY_OF_WEEK),
        currWeek,
        lessons,
        cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH)
    )
}


@Composable
fun LazyListState.isScrollingUp(): State<Boolean> {
    return produceState(initialValue = true) {
        var lastIndex = 0
        var lastScroll = Int.MAX_VALUE
        snapshotFlow {
            firstVisibleItemIndex to firstVisibleItemScrollOffset
        }.collect { (currentIndex, currentScroll) ->
            if (currentIndex != lastIndex || currentScroll != lastScroll) {
                value = currentIndex < lastIndex ||
                        (currentIndex == lastIndex && currentScroll < lastScroll)
                lastIndex = currentIndex
                lastScroll = currentScroll
            }
        }

    }
}