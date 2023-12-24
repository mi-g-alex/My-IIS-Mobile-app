package com.example.testschedule.data.remote.dto.account.study.certificate

import com.example.testschedule.domain.model.account.study.certificate.NewCertificatePlacesModel

data class NewCertificatePlacesDto(
    val places: List<Place>,
    val type: String? // СППС
) {
    data class Place(
        val id: Int, // 8
        val name: String?, // в ФСЗН
        val type: Int // 1
    ) {
        fun toModel() = NewCertificatePlacesModel.Place(
            name = this.name,
            type = this.type
        )
    }

    fun toModel() = NewCertificatePlacesModel(
        places = this.places.map { it.toModel() },
        title = this.type ?: ""
    )
}