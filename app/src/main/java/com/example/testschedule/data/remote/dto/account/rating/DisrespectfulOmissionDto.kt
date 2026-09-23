package com.example.testschedule.data.remote.dto.account.rating

import com.google.gson.annotations.SerializedName

data class DisrespectfulOmissionDto(
    @SerializedName("date") val date: String,
    @SerializedName("subject") val subject: SubjectDto,
    @SerializedName("lessonTypeAbbrev") val lessonTypeAbbrev: String,
    @SerializedName("hours") val hours: Int,
    @SerializedName("term") val term: Int
) {
    data class SubjectDto(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("abbrev") val abbrev: String
    )
}
