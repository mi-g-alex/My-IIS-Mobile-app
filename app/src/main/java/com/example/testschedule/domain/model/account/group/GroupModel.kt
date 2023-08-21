package com.example.testschedule.domain.model.account.group

import com.example.testschedule.data.local.entity.account.group.GroupEntity

data class GroupModel(
    val groupInfoStudent: List<GroupInfoStudent>,
    val numberOfGroup: String, // 253501
    val studentGroupCurator: StudentGroupCurator?
) {
    data class GroupInfoStudent(
        val fio: String, // Азаров Егор Антонович
        val position: String, // Куратор
    ) {
        fun toEntity() = GroupEntity.GroupInfoStudent(
            fio = this.fio,
            position = this.position
        )
    }

    data class StudentGroupCurator(
        val email: String?, // stroinikova@bsuir.by
        val fio: String, // Стройникова Елена Дмитриевна
        val phone: String?, // +375172938666
        val position: String, // Куратор
        val urlId: String? // e-strojnikova
    ) {
        fun toEntity() = GroupEntity.StudentGroupCurator(
            email = this.email,
            fio = this.fio,
            phone = this.phone,
            position = this.position,
            urlId = this.urlId
        )
    }

    fun toEntity() = GroupEntity(
        key = 0,
        numberOfGroup = this.numberOfGroup,
        groupInfoStudent = this.groupInfoStudent.map { it.toEntity() },
        studentGroupCurator = this.studentGroupCurator?.toEntity()
    )
}