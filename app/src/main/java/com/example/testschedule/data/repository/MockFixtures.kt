package com.example.testschedule.data.repository

import com.example.testschedule.data.remote.dto.account.study.mark_sheet.additional.MarkSheetTypeModel
import com.example.testschedule.data.remote.dto.auth.UserBasicDataDto
import com.example.testschedule.domain.model.account.dormitory.DormitoryModel
import com.example.testschedule.domain.model.account.dormitory.PrivilegesModel
import com.example.testschedule.domain.model.account.group.GroupModel
import com.example.testschedule.domain.model.account.headman.create_omissions.HeadmanGetOmissionsModel
import com.example.testschedule.domain.model.account.mark_book.MarkBookModel
import com.example.testschedule.domain.model.account.notifications.NotificationModel
import com.example.testschedule.domain.model.account.omissions.OmissionsModel
import com.example.testschedule.domain.model.account.penalty.PenaltyModel
import com.example.testschedule.domain.model.account.profile.AccountProfileModel
import com.example.testschedule.domain.model.account.rating.RatingModel
import com.example.testschedule.domain.model.account.settings.email.ContactsModel
import com.example.testschedule.domain.model.account.study.certificate.CertificateModel
import com.example.testschedule.domain.model.account.study.certificate.NewCertificatePlacesModel
import com.example.testschedule.domain.model.account.study.mark_sheet.MarkSheetModel
import com.example.testschedule.domain.model.account.study.mark_sheet.create.MarkSheetSubjectsModel
import com.example.testschedule.domain.model.account.study.mark_sheet.create.SearchEmployeeMarkSheetModel
import com.example.testschedule.domain.model.auth.LoginAndPasswordModel
import com.example.testschedule.domain.model.auth.UserBasicDataModel
import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel
import com.example.testschedule.domain.model.schedule.ListOfGroupsModel
import com.example.testschedule.domain.model.schedule.ScheduleModel

internal object MockFixtures {
    const val COOKIE = "mock-session=local"

    val credentials = LoginAndPasswordModel(
        username = "25350000",
        password = "mock-password"
    )

    val basicUser = UserBasicDataModel(
        canStudentNote = true,
        email = "student@example.com",
        fio = "Test Student",
        group = "253501",
        hasNotConfirmedContact = false,
        isGroupHead = true,
        phone = "+375290000000",
        photoUrl = null,
        username = credentials.username,
        cookie = COOKIE
    )

    val basicUserDto = UserBasicDataDto(
        authorities = emptyList(),
        canStudentNote = basicUser.canStudentNote,
        email = basicUser.email,
        fio = basicUser.fio,
        group = basicUser.group,
        hasNotConfirmedContact = basicUser.hasNotConfirmedContact,
        isGroupHead = basicUser.isGroupHead,
        phone = basicUser.phone,
        photoUrl = null,
        username = basicUser.username
    )

    val profile = AccountProfileModel(
        id = 100000,
        lastName = "Student",
        firstName = "Test",
        middleName = "Тестовый",
        photoUrl = null,
        birthDate = "01.01.2004",
        group = basicUser.group,
        faculty = "FCSN",
        speciality = "Computer Science",
        course = 3,
        rating = 8,
        bio = "Локальный тестовый аккаунт. Все данные созданы на устройстве.",
        references = listOf(
            AccountProfileModel.ReferenceModel(
                id = 1,
                name = "GitHub",
                reference = "github.com/example"
            )
        ),
        skills = listOf(
            AccountProfileModel.SkillModel(id = 1, name = "Kotlin"),
            AccountProfileModel.SkillModel(id = 2, name = "Android")
        ),
        settingPublished = true,
        settingSearchJob = true,
        settingShowRating = true,
        outlookLogin = "student@study.example.com",
        outlookPassword = "mock-only"
    )

    val notifications = listOf(
        NotificationModel(
            date = "06.09.2026 12:00:00",
            id = 1,
            isViewed = false,
            message = "Тестовое уведомление: справка готова.",
            type = "SUCCESS"
        ),
        NotificationModel(
            date = "05.09.2026 09:30:00",
            id = 2,
            isViewed = true,
            message = "Тестовое уведомление: расписание обновлено.",
            type = "INFO"
        )
    )

    val dormitory = listOf(
        DormitoryModel(
            id = 100,
            number = 4242,
            numberInQueue = 7,
            status = "Документы приняты",
            applicationDate = 1788696000000,
            acceptedDate = 1788782400000,
            settledDate = null,
            roomInfo = "Room 610, Dormitory 4",
            docContent = null,
            docReference = "mock-document.pdf",
            rejectionReason = null
        )
    )

    val privileges = listOf(
        PrivilegesModel(
            id = 200,
            studentId = profile.id,
            year = 2026,
            dormitoryPrivilegeId = 10,
            dormitoryPrivilegeCategoryId = 2,
            dormitoryPrivilegeName = "Active participation in university life",
            dormitoryPrivilegeCategoryName = "Priority",
            note = "Тестовые данные"
        )
    )

    val group = GroupModel(
        groupInfoStudent = listOf(
            GroupModel.GroupInfoStudent("Test Student", "Group head"),
            GroupModel.GroupInfoStudent("Alex Example", "Student"),
            GroupModel.GroupInfoStudent("Maria Example", "Student")
        ),
        numberOfGroup = basicUser.group,
        studentGroupCurator = GroupModel.StudentGroupCurator(
            email = "curator@example.com",
            fio = "Тестовый Куратор",
            phone = "+375170000000",
            position = "Curator",
            urlId = "mock-curator"
        )
    )

    val markBook = MarkBookModel(
        averageMark = 8.5,
        semesters = mapOf(
            5 to MarkBookModel.Semester(
                averageMark = 8.5,
                marks = listOf(
                    MarkBookModel.Semester.Mark(
                        commonMark = 7.8,
                        commonRetakes = 0.03,
                        credits = 4,
                        date = "20.06.2026",
                        formOfControl = "Exam",
                        fullSubject = "Mobile Application Development",
                        hours = "144",
                        mark = "9",
                        retakesCount = 0,
                        subject = "MAD",
                        teacher = "Тестовый Преподаватель"
                    ),
                    MarkBookModel.Semester.Mark(
                        commonMark = 8.1,
                        commonRetakes = 0.01,
                        credits = 3,
                        date = "14.06.2026",
                        formOfControl = "Credit",
                        fullSubject = "Software Testing",
                        hours = "108",
                        mark = "8",
                        retakesCount = 0,
                        subject = "ST",
                        teacher = "Test Teacher"
                    )
                )
            )
        ),
        number = basicUser.username
    )

    val omissions = listOf(
        OmissionsModel(
            dateFrom = 1788523200000,
            dateTo = 1788609600000,
            id = 1,
            name = "Medical certificate",
            note = "Тестовые данные",
            term = "5"
        )
    )

    val penalties = listOf(
        PenaltyModel(
            id = 1,
            date = "01.09.2026",
            reason = "Academic achievement",
            status = "Поощрение",
            type = "Поощрение",
            note = "Тестовая запись о поощрении"
        )
    )

    private val ratingLesson = RatingModel.Point.LessonByName.LessonsByType.Lesson(
        name = "Lab 1",
        point = "9",
        date = "03.09.2026",
        omissions = 0,
        marks = listOf(9, 8)
    )

    private val ratingSubject = RatingModel.Point.LessonByName(
        types = mapOf(
            "ЛР" to RatingModel.Point.LessonByName.LessonsByType(
                all = listOf(ratingLesson),
                countOfMarks = listOf(9, 8),
                countOfOmissions = 0
            )
        ),
        name = "Mobile Development",
        allTypes = setOf("ЛР"),
        listOfMarks = listOf(9, 8),
        countOfOmissions = 0
    )

    private val ratingSummarySubject = ratingSubject.copy(
        deadlineInfo = RatingModel.Point.LessonByName.DeadlineInfo(
            totalLabs = 3,
            submittedLabs = 1,
            deadlines = listOf(
                RatingModel.Point.LessonByName.DeadlineInfo.Deadline(
                    date = "10.09.2026",
                    taskNumber = 1
                )
            )
        ),
        percentageMarks = listOf(
            RatingModel.Point.LessonByName.PercentageMark(
                date = "05.09.2026",
                number = "75"
            )
        )
    )

    val rating = RatingModel(
        points = mapOf(
            "5" to RatingModel.Point(
                subjects = mapOf(ratingSubject.name to ratingSubject),
                listOfSubjects = setOf(ratingSubject.name),
                listOfMarks = ratingSubject.listOfMarks,
                countOfOmissions = 0
            ),
            "all_points" to RatingModel.Point(
                subjects = mapOf(ratingSummarySubject.name to ratingSummarySubject),
                listOfSubjects = setOf(ratingSummarySubject.name),
                listOfMarks = ratingSummarySubject.listOfMarks,
                countOfOmissions = 0
            )
        ),
        allPoints = setOf("5")
    )

    val certificates = listOf(
        CertificateModel(
            certificateType = "обычная",
            dateOrder = "01.09.2026",
            id = 1,
            issueDate = "03.09.2026",
            number = 1001,
            provisionPlace = "At the place of request",
            rejectionReason = null,
            status = 1
        )
    )

    val certificatePlaces = listOf(
        NewCertificatePlacesModel(
            places = listOf(
                NewCertificatePlacesModel.Place("At the place of request", 0),
                NewCertificatePlacesModel.Place("For the embassy", 1)
            ),
            title = "Common"
        )
    )

    val markSheets = listOf(
        MarkSheetModel(
            absentDate = "02.09.2026",
            createDate = "03.09.2026",
            employeeFIO = "Тестовый Преподаватель",
            expireDate = "17.09.2026",
            hours = 2.0,
            id = 1,
            type = 3,
            number = "100/2026",
            price = 0.0,
            isGoodReason = true,
            rejectionReason = "",
            retakeCount = 0,
            status = "обрабатывается",
            subjectName = "Mobile Development",
            subjectType = "ЛР",
            term = 5
        )
    )

    val markSheetTypes = listOf(
        MarkSheetTypeModel(
            coefficient = 1.0,
            fullName = "Laboratory work",
            id = 3,
            isCourseWork = false,
            isExam = false,
            isLab = true,
            isOffset = false,
            isRemote = false,
            price = 0.0,
            shortName = "ЛР"
        )
    )

    val markSheetSubjects = listOf(
        MarkSheetSubjectsModel(
            abbrev = "MAD",
            etId = 1,
            lessonTypes = listOf(
                MarkSheetSubjectsModel.LessonTypeModel(
                    abbrev = "ЛР",
                    focsId = 1,
                    isCourseWork = false,
                    isExam = false,
                    isLab = true,
                    isOffset = false,
                    isRemote = false,
                    thId = 1
                )
            ),
            term = 5
        )
    )

    val employees = listOf(
        SearchEmployeeMarkSheetModel(
            academicDepartment = "Department of Computer Science",
            fio = "Тестовый Преподаватель",
            firstName = "Тестовый",
            id = 1,
            lastName = "Teacher",
            middleName = null,
            price = 0.0
        )
    )

    val contacts = ContactsModel(
        contactDtoList = listOf(
            ContactsModel.ContactModel(
                codeExpirationTime = null,
                confirmed = true,
                contactTypeId = 6,
                contactValue = "student@example.com",
                id = 1
            )
        ),
        numberOfAttempts = 3
    )

    val groups = listOf(
        ListOfGroupsModel(
            course = 3,
            calendarId = "mock-calendar",
            facultyAbbrev = "FCSN",
            name = basicUser.group,
            specialityAbbrev = "CS",
            specialityName = "Computer Science"
        )
    )

    val scheduleEmployees = listOf(
        ListOfEmployeesModel(
            academicDepartment = listOf("Department of Computer Science"),
            calendarId = "mock-teacher-calendar",
            fio = "Тестовый Преподаватель",
            firstName = "Тестовый",
            lastName = "Teacher",
            middleName = null,
            photoLink = null,
            rank = "Lecturer",
            degree = null,
            urlId = "mock-teacher"
        )
    )

    private val scheduleTeacher = ScheduleModel.EmployeeInfo(
        id = 1,
        firstName = "Тестовый",
        middleName = null,
        lastName = "Teacher",
        photoLink = null,
        degree = null,
        degreeAbbrev = null,
        rank = "Lecturer",
        urlId = "mock-teacher"
    )

    private val scheduleLesson = ScheduleModel.WeeksSchedule.Lesson(
        auditories = listOf("101"),
        startLessonTime = 9 * 60,
        endLessonTime = 10 * 60 + 20,
        startLessonDate = null,
        endLessonDate = null,
        dateLesson = null,
        lessonTypeAbbrev = "ЛК",
        note = "Локальное тестовое занятие",
        numSubgroup = 0,
        studentGroups = listOf(
            ScheduleModel.WeeksSchedule.Lesson.StudentGroupsInfo(
                specialityName = "Computer Science",
                numberOfStudents = 24,
                name = basicUser.group,
                educationDegree = 1
            )
        ),
        subject = "MAD",
        subjectFullName = "Mobile Application Development",
        weekNumber = listOf(1, 2, 3, 4),
        employees = listOf(scheduleTeacher),
        announcement = false,
        split = false
    )

    val schedule = ScheduleModel(
        id = basicUser.group,
        title = basicUser.group,
        isGroupSchedule = true,
        startLessonsDate = null,
        endLessonsDate = null,
        startExamsDate = null,
        endExamsDate = null,
        employeeInfo = null,
        studentGroupInfo = ScheduleModel.StudentGroupInfo(
            name = basicUser.group,
            facultyAbbrev = "FCSN",
            specialityName = "Computer Science",
            specialityAbbrev = "CS",
            course = 3
        ),
        schedules = listOf(
            ScheduleModel.WeeksSchedule(
                monday = listOf(scheduleLesson),
                tuesday = emptyList(),
                wednesday = listOf(scheduleLesson.copy(
                    startLessonTime = 10 * 60 + 35,
                    endLessonTime = 11 * 60 + 55,
                    lessonTypeAbbrev = "ЛР"
                )),
                thursday = emptyList(),
                friday = emptyList(),
                saturday = emptyList()
            )
        ),
        exams = emptyList()
    )

    fun headman(date: String) = HeadmanGetOmissionsModel(
        lessons = listOf(
            HeadmanGetOmissionsModel.LessonModel(
                id = 1,
                dateString = date,
                nameAbbrev = "MAD",
                lessonTypeAbbrev = "ЛР",
                subGroup = 1,
                lessonPeriod = HeadmanGetOmissionsModel.LessonModel.LessonPeriodModel(
                    startTime = "10:35",
                    endTime = "11:55",
                    lessonPeriodHours = 2
                ),
                students = listOf(
                    HeadmanGetOmissionsModel.LessonModel.StudentModel(
                        id = 1,
                        fio = "Alex Example",
                        omission = null
                    ),
                    HeadmanGetOmissionsModel.LessonModel.StudentModel(
                        id = 2,
                        fio = "Maria Example",
                        omission = HeadmanGetOmissionsModel.LessonModel.StudentModel.OmissionModel(
                            id = 1,
                            missedHours = 2,
                            respectfulOmission = true
                        )
                    )
                )
            )
        ),
        date = date
    )
}
