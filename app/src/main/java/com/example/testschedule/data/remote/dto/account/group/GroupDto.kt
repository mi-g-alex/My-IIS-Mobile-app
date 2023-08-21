package com.example.testschedule.data.remote.dto.account.group

import com.example.testschedule.domain.model.account.group.GroupModel

data class GroupDto(
    val groupInfoStudentDto: List<GroupInfoStudent>,
    val numberOfGroup: String, // 253501
    val studentGroupCuratorDto: StudentGroupCurator?
) {
    data class GroupInfoStudent(
        val fio: String, // Азаров Егор Антонович
        val position: String?,
        val urlId: String
    ) {
        fun toModel() = GroupModel.GroupInfoStudent(
            fio = this.fio,
            position = this.position ?: ""
        )
    }

    data class StudentGroupCurator(
        val email: String?, // stroinikova@bsuir.by
        val fio: String, // Стройникова Елена Дмитриевна
        val phone: String?, // +375172938666
        val position: String, // Куратор
        val urlId: String? // e-strojnikova
    ) {
        fun toModel() = GroupModel.StudentGroupCurator(
            email = this.email,
            fio = this.fio,
            phone = this.phone,
            position = this.position,
            urlId = this.urlId
        )
    }

    fun toModel() = GroupModel(
        groupInfoStudent = this.groupInfoStudentDto.map { it.toModel() },
        numberOfGroup = this.numberOfGroup,
        studentGroupCurator = this.studentGroupCuratorDto?.toModel()
    )
}