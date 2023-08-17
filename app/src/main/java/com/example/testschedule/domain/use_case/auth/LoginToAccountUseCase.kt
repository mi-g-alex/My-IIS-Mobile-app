package com.example.testschedule.domain.use_case.auth

import com.example.testschedule.common.Resource
import com.example.testschedule.domain.model.auth.LoginAndPasswordModel
import com.example.testschedule.domain.model.auth.UserBasicDataModel
import com.example.testschedule.domain.repository.IisAPIRepository
import com.example.testschedule.domain.repository.UserDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
        var us = username
        if (us == null)
            us = db.getLoginAndPassword().username
        var pass = password
        if (pass == null)
            pass = db.getLoginAndPassword().password
        try {
            emit(Resource.Loading<UserBasicDataModel>())
            val response = api.loginToAccount(us, pass).awaitResponse()
            val cookie = response.headers()["Set-Cookie"].toString()
            val answerModel = response.body()?.toModel(cookie)
            db.setLoginAndPassword(LoginAndPasswordModel(username = us, password = pass))
            answerModel?.let { db.setUserBasicData(it) }

            emit(Resource.Success<UserBasicDataModel>(answerModel))
        } catch (e: IOException) {
            if (e.toString() == "java.io.EOFException: End of input at line 1 column 1 path \$") {
                emit(Resource.Error<UserBasicDataModel>("WrongPassword"))
            } else if (e.toString().contains("Unable to resolve host")) {
                emit(Resource.Error<UserBasicDataModel>("ConnectionFailed"))
            } else {
                emit(Resource.Error<UserBasicDataModel>("OtherError"))
            }

        } catch (e: Exception) {
            emit(Resource.Error<UserBasicDataModel>("OtherError"))
        }
    }
}