package com.example.testschedule.data.local.entity.account.study.certificate

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testschedule.domain.model.account.study.certificate.NewCertificatePlacesModel

@Entity
data class NewCertificatePlacesEntity(
    val places: List<Place>,
    val type: String?, // СППС
    @PrimaryKey
    val id: Int
) {
    data class Place(
        val name: String?, // в ФСЗН
        val type: Int
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