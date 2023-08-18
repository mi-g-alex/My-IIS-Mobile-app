package com.example.testschedule.data.local.entity.account.profile

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testschedule.domain.model.account.profile.AccountProfileModel

@Entity
data class AccountProfileEntity(
    @PrimaryKey
    val key: Int,
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
    val references: List<Reference>,
    val skills: List<Skill>,
    val settingPublished: Boolean?, // true
    val settingSearchJob: Boolean?, // false
    val settingShowRating: Boolean?, // true
    val outlookLogin: String?, // emailhere@study.bsuir.by
    val outlookPassword: String? // password here
) {
    data class Reference(
        val id: Int, // 4371xxxx
        val name: String, // TG
        val reference: String // t.me/xxxxx
    ) {
        fun toModel() = AccountProfileModel.ReferenceModel(
            id = this.id,
            name = this.name,
            reference = this.reference
        )
    }

    data class Skill(
        val id: Int, // 154
        val name: String // C/C++
    ) {
        fun toModel() = AccountProfileModel.SkillModel(
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
        outlookLogin = this.outlookLogin,
        outlookPassword = this.outlookPassword,
        photoUrl = this.photoUrl,
        rating = this.rating,
        references = this.references.map { it.toModel() },
        settingPublished = this.settingPublished,
        settingSearchJob = this.settingSearchJob,
        settingShowRating = this.settingShowRating,
        skills = this.skills.map { it.toModel() },
        speciality = this.speciality,
        group = this.group,
        bio = this.bio,
    )

}
