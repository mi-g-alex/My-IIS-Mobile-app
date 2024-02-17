package com.example.testschedule.domain.use_case.account.study.certificate

import com.example.testschedule.common.Resource
import com.example.testschedule.domain.repository.IisAPIRepository
import com.example.testschedule.domain.repository.UserDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.awaitResponse
import java.io.IOException
import javax.inject.Inject

class CloseCertificateUseCase @Inject constructor(
    private val api: IisAPIRepository,
    private val db: UserDatabaseRepository
) {

    operator fun invoke(id: Int): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            val cookie = db.getCookie()
            api.closeCertificate(id, cookie)
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
                    api.closeCertificate(id, cookie)
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