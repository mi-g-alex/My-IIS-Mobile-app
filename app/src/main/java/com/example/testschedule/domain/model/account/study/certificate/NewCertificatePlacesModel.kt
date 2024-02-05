package com.example.testschedule.domain.model.account.study.certificate

import com.example.testschedule.data.local.entity.account.study.certificate.NewCertificatePlacesEntity

data class NewCertificatePlacesModel(
    val places: List<Place>,
    val title: String // СППС
) {
    /** Type:
     *
     * 0 - любая
     *
     * 1 - гербовая только
     *
     * 2 - только гербовая и военкомат
     *
     * Там где только гебовая нельзя комментарий оставлять
     **/
    data class Place(
        val name: String?, // в ФСЗН
        val type: Int
    ) {
        fun toEntity() = NewCertificatePlacesEntity.Place(
            name = name,
            type = type
        )
    }

    fun toEntity(id: Int) = NewCertificatePlacesEntity(
        this.places.map { it.toEntity() },
        this.title,
        id
    )
}