package com.example.testschedule.domain.modal.schedule

data class ScheduleModel(
    val startLessonsDate: String?, // Дата начала занятий в этой группе
    val endLessonsDate: String?, // Дата окончания занятий в этой группе
    val startExamsDate: String?, // Дата начала экзаменов в этой группе
    val endExamsDate: String?, // Дата окончания экзаменов в этой группе
    val employeeInfo: EmployeeInfo?, // Информация о преподавателе, если это его расписание
    val studentGroupInfo : StudentGroupInfo?, // Информация о группе, если это её расписание
    val schedules : List<WeeksSchedule>, // Тут храним список из недель, где в каждой неделе если список дней, а в каждом дне список занятий
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
        val rank: String?, // доцент и т.п.
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
            val startLessonTime: String, // Время начала урока / экзамена
            val endLessonTime: String,  // Время конеца урока / экзамена
            val startLessonDate: String?, // Дата начада занятий по предмета
            val endLessonDate: String?, // Дата окончания занятий по предменту
            val dateLesson: String?, // Дата экзамена
            val lessonTypeAbbrev: String, // ЛК, ЛР, ПЗ, Консультация, Экзамен
            val note: String?, // Пометка
            val numSubgroup: Int, // Номер подгруппы: 0, 1, 2
            val studentGroups: List<StudentGroupsInfo>, // Список групп
            val subject: String, // Название предмета: МА
            val subjectFullName: String, // Полное название: Математический анализ
            val weekNumber : List<Int>, // номера недель
            val employees: List<EmployeeInfo>?, // Список преподавателей
            val announcement: Boolean, // событие или нет
            val split: Boolean // хз что это
        ) {
            data class StudentGroupsInfo(
                val specialityName: String, // Информатика и технологии программирования
                val numberOfStudents: Int, // 28
                val name: String, // 253501
                val educationDegree: Int, // 1
            )
        }
    }
}