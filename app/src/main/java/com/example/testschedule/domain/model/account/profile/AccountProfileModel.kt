package com.example.testschedule.domain.model.account.profile

import com.example.testschedule.data.local.entity.account.profile.AccountProfileEntity
import com.example.testschedule.data.remote.dto.account.profile.AccountProfileDto

data class AccountProfileModel(
    val id: Int?, // 152xx
    val lastName: String?, // Иванов
    val firstName: String?, // Иван
    val middleName: String?, // Иванович
    val photoUrl: String?, // https://drive.google.com/uc?id=1yRyNQ0sEVx3gnnzta...
    val birthDate: String?, // xx.xx.20xx
    val group: String?, // 25xxxx
    val faculty: String?, // ФКСиС
    val speciality: String?, // ИиТП
    val course: Int?, // 2
    val rating: Int?, // 5
    val bio: String?, // Text
    val references: List<ReferenceModel>,
    val skills: List<SkillModel>,
    val settingPublished: Boolean?, // true
    val settingSearchJob: Boolean?, // false
    val settingShowRating: Boolean?, // true
    val outlookLogin: String?, // emailhere@study.bsuir.by
    val outlookPassword: String? // password here
) {
    data class ReferenceModel(
        val id: Int, // 4371xxxx
        val name: String, // TG
        val reference: String // t.me/xxxxx
    ) {
        fun toEntity() = AccountProfileEntity.Reference(
            id = id,
            name = name,
            reference = reference
        )

        fun toDto() = AccountProfileDto.ReferenceDto(
            id = if (id == 0) null else id,
            name = name,
            reference = reference
        )
    }

    data class SkillModel(
        val id: Int, // 154
        val name: String // C/C++
    ) {
        fun toEntity() = AccountProfileEntity.Skill(
            id = id,
            name = name
        )

        fun toDto() = AccountProfileDto.SkillDto(
            id = if (id == 0) null else id,
            name = name
        )
    }

    fun toEntity() = AccountProfileEntity(
        key = 0,
        birthDate = birthDate,
        course = course,
        faculty = faculty,
        firstName = firstName,
        id = id,
        lastName = lastName,
        middleName = middleName,
        outlookLogin = outlookLogin,
        outlookPassword = outlookPassword,
        photoUrl = photoUrl,
        rating = rating,
        references = references.map { it.toEntity() },
        settingPublished = settingPublished,
        settingSearchJob = settingSearchJob,
        settingShowRating = settingShowRating,
        skills = skills.map { it.toEntity() },
        speciality = speciality,
        group = group,
        bio = bio,
    )

    fun toDto() = AccountProfileDto(
        birthDate = birthDate,
        course = course,
        faculty = faculty,
        firstName = firstName,
        id = id,
        lastName = lastName,
        middleName = middleName,
        officeEmail = outlookLogin,
        officePassword = outlookPassword,
        photoUrl = photoUrl,
        rating = rating,
        references = references.map { it.toDto() },
        published = settingPublished,
        searchJob = settingSearchJob,
        showRating = settingShowRating,
        skills = skills.map { it.toDto() },
        speciality = speciality,
        studentGroup = group,
        summary = bio,
    )
}
