package com.example.testschedule.common

import com.example.testschedule.data.remote.dto.schedule_view.ScheduleDto

object Constants {
    const val BASE_URL_IIS_API : String = "https://iis.bsuir.by/api/v1/"
    const val PREF_OPEN_BY_DEFAULT_ID : String = "PREF_OPEN_BY_DEFAULT_ID"
    const val PREF_OPEN_BY_DEFAULT_TITLE : String = "PREF_OPEN_BY_DEFAULT_TITLE"
    const val LAST_UPDATE_CURRENT_WEEK : String = "LAST_UPDATE_CURRENT_WEEK"
    const val CURRENT_WEEK : String = "CURRENT_WEEK"
    const val MY_PREF : String = "MY_PREF"
    val test : ScheduleDto = ScheduleDto(
        startDate = "01.09.2023",
        endDate = "21.12.2023",
        startExamsDate = "01.01.2024",
        endExamsDate = "05.02.2024",
        employeeDto = null,
        studentGroupDto = ScheduleDto.StudentGroupDto(
            name = "253501",
            facultyId = 20026,
            facultyAbbrev = "ФКСиС",
            specialityDepartmentEducationFormId = 20021,
            specialityName = "Информатика и технологии программирования",
            specialityAbbrev = "ИиТП",
            course = 1,
            id = 23871,
            calendarId = "369hj5e5k0lr1k7necn1rnfdpg@group.calendar.google.com",
            educationDegree = 1
        ),
        schedules = ScheduleDto.SchedulesDto(
            monday = null,
            tuesday = null,
            thursday = listOf(
                ScheduleDto.SchedulesDto.SchedulesItemDto(
                    auditories = listOf("501-4 к."),
                    endLessonTime = "15:00",
                    lessonTypeAbbrev = "Экзамен",
                    note = null,
                    numSubgroup = 0,
                    startLessonTime = "14:00",
                    studentGroups = listOf(ScheduleDto.SchedulesDto.SchedulesItemDto.StudentGroupsItemDto(
                        specialityName = "Информатика и технологии программирования",
                        specialityCode = "1-40 01 03",
                        numberOfStudents = 28,
                        name = "253501",
                        educationDegree = 1
                    )),
                    subject = "Физика",
                    subjectFullName = "Физика",
                    listOf(1, 2, 3, 4),
                    employees = listOf(ScheduleDto.EmployeeDto(
                        id = 545863,
                        firstName = "Мария",
                        middleName = "Сергеевна",
                        lastName = "Ильясова",
                        photoLink = "https://iis.bsuir.by/api/v1/employees/photo/545863",
                        degree = "",
                        degreeAbbrev = "",
                        rank = null,
                        email = null,
                        urlId = "m-iliasova",
                        calendarId = "ignoqgn4qtdv1iprqpv2nbulek@group.calendar.google.com",
                        jobPositions = null
                    )),
                    dateLesson = "01.02.2024",
                    startLessonDate = null,
                    endLessonDate = null,
                    announcement = false,
                    split = false
                )
            ),
            wednesday = listOf(
                ScheduleDto.SchedulesDto.SchedulesItemDto(
                    auditories = listOf("501-4 к."),
                    endLessonTime = "15:00",
                    lessonTypeAbbrev = "Консультация",
                    note = null,
                    numSubgroup = 0,
                    startLessonTime = "14:00",
                    studentGroups = listOf(ScheduleDto.SchedulesDto.SchedulesItemDto.StudentGroupsItemDto(
                        specialityName = "Информатика и технологии программирования",
                        specialityCode = "1-40 01 03",
                        numberOfStudents = 28,
                        name = "253501",
                        educationDegree = 1
                    )),
                    subject = "Физика",
                    subjectFullName = "Физика",
                    listOf(1, 2, 3, 4),
                    employees = listOf(ScheduleDto.EmployeeDto(
                        id = 545863,
                        firstName = "Мария",
                        middleName = "Сергеевна",
                        lastName = "Ильясова",
                        photoLink = "https://iis.bsuir.by/api/v1/employees/photo/545863",
                        degree = "",
                        degreeAbbrev = "",
                        rank = null,
                        email = null,
                        urlId = "m-iliasova",
                        calendarId = "ignoqgn4qtdv1iprqpv2nbulek@group.calendar.google.com",
                        jobPositions = null
                    )),
                    dateLesson = "31.01.2024",
                    startLessonDate = null,
                    endLessonDate = null,
                    announcement = false,
                    split = false
                )
            ),
            friday = null,
            saturday = listOf(
                ScheduleDto.SchedulesDto.SchedulesItemDto(
                    auditories = listOf("513-2 к."),
                    endLessonTime = "11:55",
                    lessonTypeAbbrev = "ЛР",
                    note = null,
                    numSubgroup = 1,
                    startLessonTime = "10:35",
                    studentGroups = listOf(ScheduleDto.SchedulesDto.SchedulesItemDto.StudentGroupsItemDto(
                        specialityName = "Информатика и технологии программирования",
                        specialityCode = "1-40 01 03",
                        numberOfStudents = 28,
                        name = "253501",
                        educationDegree = 1
                    )),
                    subject = "ОКГ",
                    subjectFullName = "Основы компьютерной графики",
                    listOf(2, 4),
                    employees = listOf(ScheduleDto.EmployeeDto(
                        id = 545863,
                        firstName = "Мария",
                        middleName = "Сергеевна",
                        lastName = "Ильясова",
                        photoLink = "https://iis.bsuir.by/api/v1/employees/photo/545863",
                        degree = "",
                        degreeAbbrev = "",
                        rank = null,
                        email = null,
                        urlId = "m-iliasova",
                        calendarId = "ignoqgn4qtdv1iprqpv2nbulek@group.calendar.google.com",
                        jobPositions = null
                    )),
                    dateLesson = null,
                    startLessonDate = "01.09.2023",
                    endLessonDate = "21.12.2023",
                    announcement = false,
                    split = false
                ),
                ScheduleDto.SchedulesDto.SchedulesItemDto(
                    auditories = listOf("515-2 к."),
                    endLessonTime = "13:45",
                    lessonTypeAbbrev = "ЛР",
                    note = null,
                    numSubgroup = 0,
                    startLessonTime = "12:25",
                    studentGroups = listOf(ScheduleDto.SchedulesDto.SchedulesItemDto.StudentGroupsItemDto(
                        specialityName = "Информатика и технологии программирования",
                        specialityCode = "1-40 01 03",
                        numberOfStudents = 28,
                        name = "253501",
                        educationDegree = 1
                    )),
                    subject = "ОКГ",
                    subjectFullName = "Основы компьютерной графики",
                    listOf(2, 4),
                    employees = listOf(ScheduleDto.EmployeeDto(
                        id = 545863,
                        firstName = "Мария",
                        middleName = "Сергеевна",
                        lastName = "Ильясова",
                        photoLink = "https://iis.bsuir.by/api/v1/employees/photo/545863",
                        degree = "",
                        degreeAbbrev = "",
                        rank = null,
                        email = null,
                        urlId = "m-iliasova",
                        calendarId = "ignoqgn4qtdv1iprqpv2nbulek@group.calendar.google.com",
                        jobPositions = null
                    )),
                    dateLesson = null,
                    startLessonDate = "01.09.2023",
                    endLessonDate = "21.12.2023",
                    announcement = false,
                    split = false
                ),
                ScheduleDto.SchedulesDto.SchedulesItemDto(
                    auditories = listOf("513-2 к."),
                    endLessonTime = "15:20",
                    lessonTypeAbbrev = "ЛР",
                    note = null,
                    numSubgroup = 2,
                    startLessonTime = "14:00",
                    studentGroups = listOf(ScheduleDto.SchedulesDto.SchedulesItemDto.StudentGroupsItemDto(
                        specialityName = "Информатика и технологии программирования",
                        specialityCode = "1-40 01 03",
                        numberOfStudents = 28,
                        name = "253501",
                        educationDegree = 1
                    )),
                    subject = "ОКГ",
                    subjectFullName = "Основы компьютерной графики",
                    listOf(2, 4),
                    employees = listOf(ScheduleDto.EmployeeDto(
                        id = 545863,
                        firstName = "Мария",
                        middleName = "Сергеевна",
                        lastName = "Ильясова",
                        photoLink = "https://iis.bsuir.by/api/v1/employees/photo/545863",
                        degree = "",
                        degreeAbbrev = "",
                        rank = null,
                        email = null,
                        urlId = "m-iliasova",
                        calendarId = "ignoqgn4qtdv1iprqpv2nbulek@group.calendar.google.com",
                        jobPositions = null
                    )),
                    dateLesson = null,
                    startLessonDate = "01.09.2023",
                    endLessonDate = "21.12.2023",
                    announcement = false,
                    split = false
                )
            )
        ),
        exams = listOf(
            ScheduleDto.SchedulesDto.SchedulesItemDto(
                auditories = listOf("501-4 к."),
                endLessonTime = "15:00",
                lessonTypeAbbrev = "Консультация",
                note = null,
                numSubgroup = 0,
                startLessonTime = "14:00",
                studentGroups = listOf(ScheduleDto.SchedulesDto.SchedulesItemDto.StudentGroupsItemDto(
                    specialityName = "Информатика и технологии программирования",
                    specialityCode = "1-40 01 03",
                    numberOfStudents = 28,
                    name = "253501",
                    educationDegree = 1
                )),
                subject = "Физика",
                subjectFullName = "Физика",
                listOf(1, 2, 3, 4),
                employees = listOf(ScheduleDto.EmployeeDto(
                    id = 545863,
                    firstName = "Мария",
                    middleName = "Сергеевна",
                    lastName = "Ильясова",
                    photoLink = "https://iis.bsuir.by/api/v1/employees/photo/545863",
                    degree = "",
                    degreeAbbrev = "",
                    rank = null,
                    email = null,
                    urlId = "m-iliasova",
                    calendarId = "ignoqgn4qtdv1iprqpv2nbulek@group.calendar.google.com",
                    jobPositions = null
                )),
                dateLesson = "31.01.2024",
                startLessonDate = null,
                endLessonDate = null,
                announcement = false,
                split = false
            ),
            ScheduleDto.SchedulesDto.SchedulesItemDto(
                auditories = listOf("501-4 к."),
                endLessonTime = "15:00",
                lessonTypeAbbrev = "Экзамен",
                note = null,
                numSubgroup = 0,
                startLessonTime = "14:00",
                studentGroups = listOf(ScheduleDto.SchedulesDto.SchedulesItemDto.StudentGroupsItemDto(
                    specialityName = "Информатика и технологии программирования",
                    specialityCode = "1-40 01 03",
                    numberOfStudents = 28,
                    name = "253501",
                    educationDegree = 1
                )),
                subject = "Физика",
                subjectFullName = "Физика",
                listOf(1, 2, 3, 4),
                employees = listOf(ScheduleDto.EmployeeDto(
                    id = 545863,
                    firstName = "Мария",
                    middleName = "Сергеевна",
                    lastName = "Ильясова",
                    photoLink = "https://iis.bsuir.by/api/v1/employees/photo/545863",
                    degree = "",
                    degreeAbbrev = "",
                    rank = null,
                    email = null,
                    urlId = "m-iliasova",
                    calendarId = "ignoqgn4qtdv1iprqpv2nbulek@group.calendar.google.com",
                    jobPositions = null
                )),
                dateLesson = "01.02.2024",
                startLessonDate = null,
                endLessonDate = null,
                announcement = false,
                split = false
            )
        )
    )
}