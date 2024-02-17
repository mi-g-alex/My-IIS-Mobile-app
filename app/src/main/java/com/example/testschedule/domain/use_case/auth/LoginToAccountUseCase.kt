package com.example.testschedule.domain.use_case.auth

import com.example.testschedule.common.Resource
import com.example.testschedule.domain.model.auth.LoginAndPasswordModel
import com.example.testschedule.domain.model.auth.UserBasicDataModel
import com.example.testschedule.domain.repository.IisAPIRepository
import com.example.testschedule.domain.repository.UserDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.awaitResponse
import java.io.IOException
import javax.inject.Inject

class LoginToAccountUseCase @Inject constructor(
    private val api: IisAPIRepository,
    private val db: UserDatabaseRepository
) {

    operator fun invoke(
        username: String? = null,
        password: String? = null
    ): Flow<Resource<UserBasicDataModel>> = flow {

        val us = username ?: db.getLoginAndPassword().username
        val pass = password ?: db.getLoginAndPassword().password

        try {
            emit(Resource.Loading<UserBasicDataModel>())

            val response = api.loginToAccount(us, pass).awaitResponse()

            if (response.isSuccessful) {
                val cookie = response.headers()["Set-Cookie"].toString()

                val answerModel = response.body()?.toModel(cookie)

                db.setLoginAndPassword(LoginAndPasswordModel(username = us, password = pass))
                answerModel?.let { db.setUserBasicData(it) }

                emit(Resource.Success<UserBasicDataModel>(answerModel))
                return@flow
            } else {
                throw HttpException(response)
            }
        } catch (e: HttpException) {
            if (e.code() == 401) {
                db.deleteUserBasicData()
                emit(Resource.Error<UserBasicDataModel>("WrongPassword"))
                return@flow
            }
            if (e.code() >= 500) {
                emit(Resource.Error<UserBasicDataModel>("ConnectionFailed"))
                return@flow
            }
        } catch (e: IOException) {
            emit(Resource.Error<UserBasicDataModel>("ConnectionFailed"))
        } catch (e: Exception) {
            emit(Resource.Error<UserBasicDataModel>("OtherError"))
        }
    }
}