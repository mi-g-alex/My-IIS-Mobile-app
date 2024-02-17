package com.example.testschedule.domain.use_case.account.study.mark_sheet.create

import com.example.testschedule.common.Resource
import com.example.testschedule.data.remote.dto.account.study.mark_sheet.additional.MarkSheetTypeModel
import com.example.testschedule.domain.model.account.study.mark_sheet.create.CreateMarkSheetModel
import com.example.testschedule.domain.model.account.study.mark_sheet.create.SearchEmployeeMarkSheetModel
import com.example.testschedule.domain.repository.IisAPIRepository
import com.example.testschedule.domain.repository.UserDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.awaitResponse
import java.io.IOException
import javax.inject.Inject

class CreateMarkSheetUseCase @Inject constructor(
    private val api: IisAPIRepository,
    private val db: UserDatabaseRepository
) {

    operator fun invoke(
        price: Double,
        markSheetType: MarkSheetTypeModel,
        isGoodReason: Boolean,
        hours: String,
        focsId: Int?,
        thId: Int?,
        absentDate: String,
        employee: SearchEmployeeMarkSheetModel
    ): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            val cookie = db.getCookie()
            val markSheet = CreateMarkSheetModel(
                price = price,
                markSheetType = markSheetType,
                reason = if (isGoodReason) 1 else 2,
                hours = hours,
                subject = CreateMarkSheetModel.SubjectModel(focsId, thId),
                absentDate = absentDate,
                employee = employee
            )
            api.createMarkSheet(markSheet, cookie).awaitResponse()
            emit(Resource.Success(true))
        } catch (e: HttpException) {
            if (e.code() == 403) {
                val us = db.getLoginAndPassword().username
                val pass = db.getLoginAndPassword().password
                try {
                    val response = api.loginToAccount(us, pass).awaitResponse()
                    if (!response.isSuccessful) throw HttpException(response)
                    val cookie = response.headers()["Set-Cookie"].toString()
                    response.body()?.toModel(cookie)?.let { db.setUserBasicData(it) }
                    val markSheet = CreateMarkSheetModel(
                        price = price,
                        markSheetType = markSheetType,
                        reason = if (isGoodReason) 1 else 2,
                        hours = hours,
                        subject = CreateMarkSheetModel.SubjectModel(focsId, thId),
                        absentDate = absentDate,
                        employee = employee
                    )
                    api.createMarkSheet(markSheet, cookie).awaitResponse()
                    emit(Resource.Success(true))
                } catch (e: HttpException) {
                    if (e.code() == 401) {
                        db.deleteUserBasicData()
                        emit(Resource.Error("WrongPassword"))
                    } else if (e.code() >= 500) {
                        emit(Resource.Error("ConnectionFailed"))
                    } else {
                        emit(Resource.Error("OtherError"))
                    }
                } catch (e: IOException) {
                    emit(Resource.Error("ConnectionFailed"))
                } catch (e: Exception) {
                    emit(Resource.Error("OtherError"))
                }
            } else {
                emit(Resource.Error("ConnectionFailed"))
            }
        } catch (e: IOException) {
            emit(Resource.Error("ConnectionFailed"))
        } catch (e: Exception) {
            emit(Resource.Error("OtherError"))
        }

    }

}