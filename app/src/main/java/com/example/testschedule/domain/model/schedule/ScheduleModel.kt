package com.example.testschedule.domain.model.schedule

import com.example.testschedule.data.remote.dto.schedule_view.ScheduleDto
import java.text.SimpleDateFormat
import java.util.Locale

data class ScheduleModel(
    val id: String, // Тут ID как номер группы или urlId
    val title: String, // Что будет в шапке
    val isGroupSchedule: Boolean, // Группа да или нет
    val startLessonsDate: Long?, // Дата начала занятий в этой группе в формате dd.MM.yyyy
    val endLessonsDate: Long?, // Дата окончания занятий в этой группе
    val startExamsDate: Long?, // Дата начала экзаменов в этой группе
    val endExamsDate: Long?, // Дата окончания экзаменов в этой группе
    val employeeInfo: EmployeeInfo?, // Информация о преподавателе, если это его расписание
    val studentGroupInfo: StudentGroupInfo?, // Информация о группе, если это её расписание
    val schedules: List<WeeksSchedule>, // Тут храним список из недель, где в каждой неделе есть список дней, а в каждом дне список занятий
    val exams: List<WeeksSchedule.Lesson>? // Тут список храним экзаменов
) {
    data class EmployeeInfo(
        val id: Int, // ID преподавателя
        val firstName: String, // Сергей
        val middleName: String?, // Николаевич
        val lastName: String, // Нестеренков
        val photoLink: String?, // https://iis.bsuir.by/api/v1/employees/photo/501822 (ля какой)
        val degree: String?, // кандидат технических наук и т.п.
        val degreeAbbrev: String?, // кратко? к.т.н.
        val rank: String?, // доцент и т.п.h
        val urlId: String, // s-nesterenkov
    )

    data class StudentGroupInfo(
        val name: String, // 253501
        val facultyAbbrev: String, // ФКСиС
        val specialityName: String, // Информатика и технологии программирования
        val specialityAbbrev: String, // ИиТП
        val course: Int // 1
    )

    data class WeeksSchedule( // Список предметов на этой неделе в определенный день
        val monday: List<Lesson>,
        val tuesday: List<Lesson>,
        val wednesday: List<Lesson>,
        val thursday: List<Lesson>,
        val friday: List<Lesson>,
        val saturday: List<Lesson>
    ) {
        data class Lesson(
            val auditories: List<String>, // Список кабинетов
            val startLessonTime: Int, // Время начала урока / экзамена
            val endLessonTime: Int,  // Время конеца урока / экзамена
            val startLessonDate: Long?, // Дата начада занятий по предмета
            val endLessonDate: Long?, // Дата окончания занятий по предменту
            val dateLesson: Long?, // Дата экзамена
            val lessonTypeAbbrev: String, // ЛК, ЛР, ПЗ, Консультация, Экзамен
            val note: String?, // Пометка
            val numSubgroup: Int, // Номер подгруппы: 0, 1, 2
            val studentGroups: List<StudentGroupsInfo>, // Список групп
            val subject: String, // Название предмета: МА
            val subjectFullName: String, // Полное название: Математический анализ
            val weekNumber: List<Int>, // номера недель
            val employees: List<EmployeeInfo>?, // Список преподавателей
            val announcement: Boolean, // событие или нет
            val split: Boolean // хз что это
        ) {
            data class StudentGroupsInfo(
                val specialityName: String, // Информатика и Технологии Программирования
                val numberOfStudents: Int, // 28
                val name: String, // 253501
                val educationDegree: Int, // 1
            )
        }
    }
}

fun scheduleFromDtoToModel(schedule: ScheduleDto): ScheduleModel {
    val isGroupSchedule = schedule.studentGroupDto != null && schedule.employeeDto == null

    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    val startLessonsDate =
        if (schedule.startDate.isNullOrBlank()) null else dateFormat.parse(schedule.startDate)?.time
    val endLessonsDate =
        if (schedule.endDate.isNullOrBlank()) null else dateFormat.parse(schedule.endDate)?.time
    val startExamsDate =
        if (schedule.startExamsDate.isNullOrBlank()) null else dateFormat.parse(schedule.startExamsDate)?.time
    val endExamsDate =
        if (schedule.endExamsDate.isNullOrBlank()) null else dateFormat.parse(schedule.endExamsDate)?.time

    val employeeTempInfo = schedule.employeeDto
    val employeeInfo = if (employeeTempInfo != null) {
        ScheduleModel.EmployeeInfo(
            id = employeeTempInfo.id,
            firstName = employeeTempInfo.firstName,
            middleName = employeeTempInfo.middleName,
            lastName = employeeTempInfo.lastName,
            photoLink = employeeTempInfo.photoLink,
            degree = employeeTempInfo.degree,
            degreeAbbrev = employeeTempInfo.degreeAbbrev,
            rank = employeeTempInfo.rank,
            urlId = employeeTempInfo.urlId
        )
    } else null

    val groupTempInfo = schedule.studentGroupDto
    val groupInfo = if (groupTempInfo != null) {
        ScheduleModel.StudentGroupInfo(
            name = groupTempInfo.name,
            facultyAbbrev = groupTempInfo.facultyAbbrev,
            specialityAbbrev = groupTempInfo.specialityAbbrev,
            specialityName = groupTempInfo.specialityName,
            course = groupTempInfo.course
        )
    } else null

    data class Week(
        val monday: MutableList<ScheduleModel.WeeksSchedule.Lesson>,
        val tuesday: MutableList<ScheduleModel.WeeksSchedule.Lesson>,
        val wednesday: MutableList<ScheduleModel.WeeksSchedule.Lesson>,
        val thursday: MutableList<ScheduleModel.WeeksSchedule.Lesson>,
        val friday: MutableList<ScheduleModel.WeeksSchedule.Lesson>,
        val saturday: MutableList<ScheduleModel.WeeksSchedule.Lesson>
    ) {
        fun toWeekList() = ScheduleModel.WeeksSchedule(
            this.monday.toList(),
            this.tuesday.toList(),
            this.wednesday.toList(),
            this.thursday.toList(),
            this.friday.toList(),
            this.saturday.toList()
        )
    }

    val week1 = Week(
        mutableListOf(), mutableListOf(),
        mutableListOf(), mutableListOf(),
        mutableListOf(), mutableListOf()
    )
    val week2 = Week(
        mutableListOf(), mutableListOf(),
        mutableListOf(), mutableListOf(),
        mutableListOf(), mutableListOf()
    )
    val week3 = Week(
        mutableListOf(), mutableListOf(),
        mutableListOf(), mutableListOf(),
        mutableListOf(), mutableListOf()
    )
    val week4 = Week(
        mutableListOf(), mutableListOf(),
        mutableListOf(), mutableListOf(),
        mutableListOf(), mutableListOf()
    )

    val listOfWeeks = listOf(week1, week2, week3, week4)

    schedule.schedules?.monday?.forEach { day ->
        val tmp = convertDay(day)
        if (day.weekNumber != null) {
            day.weekNumber.forEach { week ->
                listOfWeeks[week - 1].monday.add(tmp)
            }
        } else {
            listOfWeeks[0].monday.add(tmp)
            listOfWeeks[1].monday.add(tmp)
            listOfWeeks[2].monday.add(tmp)
            listOfWeeks[3].monday.add(tmp)
        }
    }

    schedule.schedules?.tuesday?.forEach { day ->
        val tmp = convertDay(day)
        if (day.weekNumber != null) {
            day.weekNumber.forEach { week ->
                listOfWeeks[week - 1].tuesday.add(tmp)
            }
        } else {
            listOfWeeks[0].tuesday.add(tmp)
            listOfWeeks[1].tuesday.add(tmp)
            listOfWeeks[2].tuesday.add(tmp)
            listOfWeeks[3].tuesday.add(tmp)
        }
    }

    schedule.schedules?.wednesday?.forEach { day ->
        val tmp = convertDay(day)
        if (day.weekNumber != null) {
            day.weekNumber.forEach { week ->
                listOfWeeks[week - 1].wednesday.add(tmp)
            }
        } else {
            listOfWeeks[0].wednesday.add(tmp)
            listOfWeeks[1].wednesday.add(tmp)
            listOfWeeks[2].wednesday.add(tmp)
            listOfWeeks[3].wednesday.add(tmp)
        }
    }

    schedule.schedules?.thursday?.forEach { day ->
        val tmp = convertDay(day)
        if (day.weekNumber != null) {
            day.weekNumber.forEach { week ->
                listOfWeeks[week - 1].thursday.add(tmp)
            }
        } else {
            listOfWeeks[0].thursday.add(tmp)
            listOfWeeks[1].thursday.add(tmp)
            listOfWeeks[2].thursday.add(tmp)
            listOfWeeks[3].thursday.add(tmp)
        }
    }

    schedule.schedules?.friday?.forEach { day ->
        val tmp = convertDay(day)
        if (day.weekNumber != null) {
            day.weekNumber.forEach { week ->
                listOfWeeks[week - 1].friday.add(tmp)
            }
        } else {
            listOfWeeks[0].friday.add(tmp)
            listOfWeeks[1].friday.add(tmp)
            listOfWeeks[2].friday.add(tmp)
            listOfWeeks[3].friday.add(tmp)
        }
    }

    schedule.schedules?.saturday?.forEach { day ->
        val tmp = convertDay(day)
        if (day.weekNumber != null) {
            day.weekNumber.forEach { week ->
                listOfWeeks[week - 1].saturday.add(tmp)
            }
        } else {
            listOfWeeks[0].saturday.add(tmp)
            listOfWeeks[1].saturday.add(tmp)
            listOfWeeks[2].saturday.add(tmp)
            listOfWeeks[3].saturday.add(tmp)
        }
    }

    listOfWeeks.forEach {
        it.monday.sortBy { it1 -> it1.startLessonTime }
        it.tuesday.sortBy { it1 -> it1.startLessonTime }
        it.wednesday.sortBy { it1 -> it1.startLessonTime }
        it.thursday.sortBy { it1 -> it1.startLessonTime }
        it.friday.sortBy { it1 -> it1.startLessonTime }
        it.saturday.sortBy { it1 -> it1.startLessonTime }
    }


    val schedules = listOfWeeks.map { it.toWeekList() }

    val exams = schedule.exams?.map { convertDay(it) }
    val empFio = if (schedule.employeeDto != null) {
        schedule.employeeDto.lastName + " " + schedule.employeeDto.firstName[0] + "." + if (schedule.employeeDto.middleName != null) {
            " " + schedule.employeeDto.middleName[0] + "."
        } else {
            ""
        }
    } else {
        ""
    }

    return ScheduleModel(
        id = if (isGroupSchedule) schedule.studentGroupDto?.name
            ?: "" else schedule.employeeDto?.urlId ?: "",
        title = if (isGroupSchedule) schedule.studentGroupDto?.name ?: "" else empFio,
        isGroupSchedule = isGroupSchedule,
        startLessonsDate = startLessonsDate,
        endLessonsDate = endLessonsDate,
        startExamsDate = startExamsDate,
        endExamsDate = endExamsDate,
        employeeInfo = employeeInfo,
        studentGroupInfo = groupInfo,
        schedules = schedules,
        exams = exams
    )
}

fun timeToInt(time: String): Int {
    val parts = time.split(":")
    return if (parts.size == 2) {
        try {
            val hours = parts[0].toInt()
            val minutes = parts[1].toInt()
            hours * 60 + minutes
        } catch (_: Exception) {
            0
        }
    } else {
        0
    }
}

fun convertDay(
    day: ScheduleDto.SchedulesDto.SchedulesItemDto
): ScheduleModel.WeeksSchedule.Lesson {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    val startLessonTime = timeToInt(day.startLessonTime)
    val endLessonTime = timeToInt(day.endLessonTime)

    val startLessonDate =
        if (day.startLessonDate.isNullOrBlank()) null else dateFormat.parse(day.startLessonDate)?.time
    val endLessonDate =
        if (day.endLessonDate.isNullOrBlank()) null else dateFormat.parse(day.endLessonDate)?.time
    val dateLesson =
        if (day.dateLesson.isNullOrBlank()) null else dateFormat.parse(day.dateLesson)?.time

    val studentGroups = day.studentGroups.map {
        ScheduleModel.WeeksSchedule.Lesson.StudentGroupsInfo(
            specialityName = it.specialityName,
            numberOfStudents = it.numberOfStudents,
            name = it.name,
            educationDegree = it.educationDegree
        )
    }
    val employees = day.employees?.map {
        ScheduleModel.EmployeeInfo(
            id = it.id,
            firstName = it.firstName,
            middleName = it.middleName,
            lastName = it.lastName,
            photoLink = it.photoLink,
            degree = it.degree,
            degreeAbbrev = it.degreeAbbrev,
            rank = it.rank,
            urlId = it.urlId
        )
    }
    return ScheduleModel.WeeksSchedule.Lesson(
        auditories = day.auditories,
        startLessonTime = startLessonTime,
        endLessonTime = endLessonTime,
        startLessonDate = startLessonDate,
        endLessonDate = endLessonDate,
        dateLesson = dateLesson,
        lessonTypeAbbrev = day.lessonTypeAbbrev ?: "",
        note = day.note,
        numSubgroup = day.numSubgroup,
        studentGroups = studentGroups,
        subject = day.subject ?: "",
        subjectFullName = day.subjectFullName ?: "",
        weekNumber = day.weekNumber ?: emptyList(),
        employees = employees,
        announcement = day.announcement,
        split = day.split,
    )
}