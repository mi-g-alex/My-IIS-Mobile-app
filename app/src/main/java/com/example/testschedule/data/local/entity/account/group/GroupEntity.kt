package com.example.testschedule.data.local.entity.account.group

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testschedule.domain.model.account.group.GroupModel

@Entity
data class GroupEntity(
    @PrimaryKey
    val key: Int,
    val numberOfGroup: String, // 253501
    val groupInfoStudent: List<GroupInfoStudent>,
    val studentGroupCurator: StudentGroupCurator?
) {
    data class GroupInfoStudent(
        val fio: String, // Азаров Егор Антонович
        val position: String, // Куратор
    ) {
        fun toModel() = GroupModel.GroupInfoStudent(
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
        fun toModel() = GroupModel.StudentGroupCurator(
            email = this.email,
            fio = this.fio,
            phone = this.phone,
            position = this.position,
            urlId = this.urlId
        )
    }

    fun toModel() = GroupModel(
        numberOfGroup = this.numberOfGroup,
        groupInfoStudent = this.groupInfoStudent.map { it.toModel() },
        studentGroupCurator = this.studentGroupCurator?.toModel()
    )
}