package com.example.testschedule.data.remote.dto.account.dormitory

import com.example.testschedule.domain.model.account.dormitory.PrivilegesModel

data class PrivilegesDto(
    val dormitoryPrivilegeCategoryId: Int?, // 3
    val dormitoryPrivilegeCategoryName: String?, // Приоритетное право
    val dormitoryPrivilegeId: Int?, // 22
    val dormitoryPrivilegeName: String?, // Принимающие активное участие в общественной жизни университета (по представлению соответствующей организации)
    val id: Int, // 6335
    val note: String?, // Кай информатики 4435
    val studentId: Int?, // 543559
    val year: Int? // 2023
) {
    fun toModel() = PrivilegesModel(
        dormitoryPrivilegeCategoryId = this.dormitoryPrivilegeCategoryId,
        dormitoryPrivilegeCategoryName = this.dormitoryPrivilegeCategoryName,
        dormitoryPrivilegeId = this.dormitoryPrivilegeId,
        dormitoryPrivilegeName = this.dormitoryPrivilegeName,
        id = this.id,
        note = this.note,
        studentId = this.studentId,
        year = this.year,
    )
}