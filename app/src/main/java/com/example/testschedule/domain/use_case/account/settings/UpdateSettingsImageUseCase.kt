package com.example.testschedule.domain.use_case.account.settings

import android.util.Log
import com.example.testschedule.common.Resource
import com.example.testschedule.domain.repository.IisAPIRepository
import com.example.testschedule.domain.repository.UserDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.awaitResponse
import java.io.IOException
import javax.inject.Inject

class UpdateSettingsImageUseCase @Inject constructor(
    private val api: IisAPIRepository,
    private val db: UserDatabaseRepository
) {

    operator fun invoke(
        image: String
    ) : Flow<Resource<String>> = flow {

        val img = "data:image/jpeg;base64,$image"

        try {
            emit(Resource.Loading())

            val cookie = db.getCookie()
            val res = api.settingsUpdatePhoto(img, cookie).awaitResponse()
            Log.e("PHOTOUPDATE", "END")
            Log.e("PHOTOUPDATE", res.message().toString())
            if (res.isSuccessful)
                emit(Resource.Success(res.body().toString()))
            else
                throw HttpException(res)
        } catch (e: HttpException) {
            Log.e("PHOTOUPDATE", e.message.toString())
            if (e.code() == 403) {
                val us = db.getLoginAndPassword().username
                val pass = db.getLoginAndPassword().password
                try {
                    val response = api.loginToAccount(us, pass).awaitResponse()
                    if (!response.isSuccessful) throw HttpException(response)
                    val cookie = response.headers()["Set-Cookie"].toString()
                    response.body()?.toModel(cookie)?.let { db.setUserBasicData(it) }

                    val res = api.settingsUpdatePhoto(img, cookie).awaitResponse()
                    if (res.isSuccessful)
                        emit(Resource.Success(res.body().toString()))
                    else
                        throw HttpException(res)
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