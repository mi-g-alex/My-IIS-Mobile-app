package com.example.testschedule.domain.model.account.profile

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
    )

    data class SkillModel(
        val id: Int, // 154
        val name: String // C/C++
    )
}
