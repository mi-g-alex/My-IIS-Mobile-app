package com.example.testschedule.domain.use_case.account.settings.email

import com.example.testschedule.common.Resource
import com.example.testschedule.domain.model.account.settings.email.SendConfirmCodeRequestModel
import com.example.testschedule.domain.repository.IisAPIRepository
import com.example.testschedule.domain.repository.UserDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.awaitResponse
import java.io.IOException
import javax.inject.Inject

/** WRONG_CODE **/
class EmailSettingsConfirmCodeUseCase @Inject constructor(
    private val api: IisAPIRepository,
    private val db: UserDatabaseRepository
) {

    operator fun invoke(
        code: String,
        id: Int
    ): Flow<Resource<Boolean>> = flow {

        val model = SendConfirmCodeRequestModel(
            code = code,
            contactId = id
        )

        try {
            emit(Resource.Loading())

            val cookie = db.getCookie()
            val res = api.settingsEmailConfirmMessage(model, cookie).awaitResponse()
            if (res.isSuccessful)
                emit(Resource.Success(true))
            else
                throw HttpException(res)
        } catch (e: HttpException) {
            if (e.code() == 409) emit(Resource.Error("WRONG_CODE"))
            else if (e.code() == 403) {
                val us = db.getLoginAndPassword().username
                val pass = db.getLoginAndPassword().password
                try {
                    val response = api.loginToAccount(us, pass).awaitResponse()
                    if (!response.isSuccessful) throw HttpException(response)
                    val cookie = response.headers()["Set-Cookie"].toString()
                    response.body()?.toModel(cookie)?.let { db.setUserBasicData(it) }

                    val res = api.settingsEmailConfirmMessage(model, cookie).awaitResponse()
                    if (res.isSuccessful)
                        emit(Resource.Success(true))
                    else
                        throw HttpException(res)
                } catch (e: HttpException) {
                    if (e.code() == 409)
                        emit(Resource.Error("WRONG_CODE"))
                    else if (e.code() == 401) {
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