package com.example.testschedule.data.remote.dto.account.dormitory

import com.example.testschedule.domain.model.account.dormitory.DormitoryModel
import java.text.SimpleDateFormat
import java.util.Locale

data class DormitoryDto(
    val acceptedDate: String?,    // "2022-08-16T17:27:02.888" — было Long?
    val applicationDate: String?, // "2022-08-16T11:39:13.127" — было Long?
    val docContent: Any?,
    val docReference: String?,
    val id: Int,
    val number: Int?,
    val numberInQueue: Any?,
    val rejectionReason: String?,
    val roomInfo: String?,
    val settledDate: String?,     // "2022-08-22T12:04:01.689" — было Long?
    val status: String?
) {
    private fun String?.toTimestamp(): Long? {
        if (this == null) return null
        return try {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
                .parse(this)?.time
        } catch (e: Exception) {
            0L
        }
    }

    fun toModel() = DormitoryModel(
        acceptedDate = this.acceptedDate.toTimestamp(),
        applicationDate = this.applicationDate.toTimestamp(),
        docContent = this.docContent,
        docReference = this.docReference,
        id = this.id,
        number = this.number,
        numberInQueue = this.numberInQueue,
        rejectionReason = this.rejectionReason,
        roomInfo = this.roomInfo,
        settledDate = this.settledDate.toTimestamp(),
        status = this.status,
    )
}
