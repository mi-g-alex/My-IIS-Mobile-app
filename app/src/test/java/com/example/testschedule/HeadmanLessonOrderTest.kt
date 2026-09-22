package com.example.testschedule

import com.example.testschedule.data.repository.orderHeadmanLessonsBySchedule
import com.example.testschedule.domain.model.account.headman.create_omissions.HeadmanGetOmissionsModel
import com.example.testschedule.domain.model.schedule.ScheduleModel
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar
import java.util.GregorianCalendar

class HeadmanLessonOrderTest {
    @Test
    fun lessonsAreEnrichedAndSortedByScheduleTime() {
        val referenceDate = GregorianCalendar(2026, Calendar.SEPTEMBER, 21).timeInMillis
        val schedule = schedule(
            scheduleLesson(
                subject = "PHY",
                fullName = "Physics",
                type = "LR",
                start = 9 * 60,
                end = 10 * 60 + 20
            ),
            scheduleLesson(
                subject = "MATH",
                fullName = "Mathematics",
                type = "PZ",
                start = 12 * 60 + 25,
                end = 13 * 60 + 45
            )
        )
        val lessons = listOf(
            headmanLesson(id = 2, name = "Mathematics", type = "PZ"),
            headmanLesson(id = 3, name = "Algorithms", type = "LR"),
            headmanLesson(id = 1, name = "Physics", type = "LR")
        )

        val result = orderHeadmanLessonsBySchedule(
            lessons = lessons,
            date = "2026-09-21",
            schedule = schedule,
            currentWeek = 1,
            referenceDateMillis = referenceDate
        )

        assertEquals(listOf("Physics", "Mathematics", "Algorithms"), result.map { it.nameAbbrev })
        assertEquals(listOf("09:00", "12:25", ""), result.map { it.lessonPeriod.startTime })
        assertEquals(listOf("10:20", "13:45", ""), result.map { it.lessonPeriod.endTime })
        assertEquals(
            listOf("Petrov Ivan Ivanovich"),
            result.first().scheduleInfo?.teacherFullNames
        )
    }

    @Test
    fun unavailableScheduleFallsBackToAlphabeticalOrder() {
        val lessons = listOf(
            headmanLesson(id = 2, name = "Mathematics", type = "PZ"),
            headmanLesson(id = 1, name = "Algorithms", type = "LR")
        )

        val result = orderHeadmanLessonsBySchedule(
            lessons = lessons,
            date = "2026-09-21",
            schedule = null,
            currentWeek = null
        )

        assertEquals(listOf("Algorithms", "Mathematics"), result.map { it.nameAbbrev })
    }

    private fun headmanLesson(
        id: Int,
        name: String,
        type: String
    ) = HeadmanGetOmissionsModel.LessonModel(
        id = id,
        dateString = "2026-09-21",
        nameAbbrev = name,
        lessonTypeAbbrev = type,
        subGroup = 0,
        lessonPeriod = HeadmanGetOmissionsModel.LessonModel.LessonPeriodModel(
            startTime = "",
            endTime = "",
            lessonPeriodHours = 2
        ),
        students = emptyList()
    )

    private fun schedule(vararg mondayLessons: ScheduleModel.WeeksSchedule.Lesson): ScheduleModel {
        val emptyWeek = ScheduleModel.WeeksSchedule(
            monday = emptyList(),
            tuesday = emptyList(),
            wednesday = emptyList(),
            thursday = emptyList(),
            friday = emptyList(),
            saturday = emptyList()
        )
        val firstWeek = emptyWeek.copy(monday = mondayLessons.toList())

        return ScheduleModel(
            id = "353501",
            title = "353501",
            isGroupSchedule = true,
            startLessonsDate = null,
            endLessonsDate = null,
            startExamsDate = null,
            endExamsDate = null,
            employeeInfo = null,
            studentGroupInfo = null,
            schedules = listOf(firstWeek, emptyWeek, emptyWeek, emptyWeek),
            exams = null
        )
    }

    private fun scheduleLesson(
        subject: String,
        fullName: String,
        type: String,
        start: Int,
        end: Int
    ) = ScheduleModel.WeeksSchedule.Lesson(
        auditories = emptyList(),
        startLessonTime = start,
        endLessonTime = end,
        startLessonDate = null,
        endLessonDate = null,
        dateLesson = null,
        lessonTypeAbbrev = type,
        note = null,
        numSubgroup = 0,
        studentGroups = emptyList(),
        subject = subject,
        subjectFullName = fullName,
        weekNumber = listOf(1),
        employees = listOf(
            ScheduleModel.EmployeeInfo(
                id = 1,
                firstName = "Ivan",
                middleName = "Ivanovich",
                lastName = "Petrov",
                photoLink = null,
                degree = null,
                degreeAbbrev = null,
                rank = null,
                urlId = "i-petrov"
            )
        ),
        announcement = false,
        split = false
    )
}
