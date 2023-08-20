package com.example.testschedule.domain.model.account.dormitory

data class PrivilegesModel(
    val id: Int, // 6335
    val studentId: Int?, // 543559
    val year: Int?, // 2023
    val dormitoryPrivilegeId: Int?, // 22
    val dormitoryPrivilegeCategoryId: Int?, // 3
    val dormitoryPrivilegeName: String?, // Принимающие активное участие в общественной жизни университета (по представлению соответствующей организации)
    val dormitoryPrivilegeCategoryName: String?, // Приоритетное право
    val note: String? // Каф информатики 4435
)