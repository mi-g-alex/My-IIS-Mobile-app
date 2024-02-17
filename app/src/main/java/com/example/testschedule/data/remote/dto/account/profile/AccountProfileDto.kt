package com.example.testschedule.data.remote.dto.account.profile

import com.example.testschedule.domain.model.account.profile.AccountProfileModel
import com.example.testschedule.domain.model.account.profile.AccountProfileModel.ReferenceModel
import com.example.testschedule.domain.model.account.profile.AccountProfileModel.SkillModel

data class AccountProfileDto(
    val birthDate: String?, // xx.xx.20xx
    val course: Int?, // 1
    val faculty: String?, // ФКСиС
    val firstName: String?, // Иван
    val id: Int?, // 152xx
    val lastName: String?, // Иванов
    val middleName: String?, // Иванович
    val officeEmail: String?, // emailhere@study.bsuir.by
    val officePassword: String?, // password here
    val photoUrl: String?, // https://drive.google.com/uc?id=1yRyNQ0sEVx3gnnzta...
    val rating: Int?, // 5
    val references: List<ReferenceDto>?,
    val published: Boolean?, // true
    val searchJob: Boolean?, // false
    val showRating: Boolean?, // true
    val skills: List<SkillDto>?,
    val speciality: String?, // ИиТП
    val studentGroup: String?, // 25xxxx
    val summary: String? // Text
) {
    data class ReferenceDto(
        val id: Int, // 4371xxxx
        val name: String, // TG
        val reference: String // t.me/xxxx
    ) {
        fun toModel() = ReferenceModel(
            id = this.id,
            name = this.name,
            reference = this.reference
        )
    }

    data class SkillDto(
        val id: Int, // 154
        val name: String // C/C++
    ) {
        fun toModel() = SkillModel(
            id = this.id,
            name = this.name
        )
    }

    fun toModel() = AccountProfileModel(
        birthDate = this.birthDate,
        course = this.course,
        faculty = this.faculty,
        firstName = this.firstName,
        id = this.id,
        lastName = this.lastName,
        middleName = this.middleName,
        outlookLogin = this.officeEmail,
        outlookPassword = this.officePassword,
        photoUrl = this.photoUrl,
        rating = this.rating,
        references = this.references?.map { it.toModel() } ?: emptyList(),
        settingPublished = this.published,
        settingSearchJob = this.searchJob,
        settingShowRating = this.showRating,
        skills = this.skills?.map { it.toModel() } ?: emptyList(),
        speciality = this.speciality,
        group = this.studentGroup,
        bio = this.summary,
    )
}
