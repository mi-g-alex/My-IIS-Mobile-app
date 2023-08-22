package com.example.testschedule.domain.use_case.account.notifications

import android.util.Log
import com.example.testschedule.common.Resource
import com.example.testschedule.domain.model.auth.LoginAndPasswordModel
import com.example.testschedule.domain.repository.IisAPIRepository
import com.example.testschedule.domain.repository.UserDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.awaitResponse
import java.io.IOException
import javax.inject.Inject

class ReadNotificationsUseCase @Inject constructor(
    private val api: IisAPIRepository,
    private val db: UserDatabaseRepository,
) {
    operator fun invoke(list: List<Int>)  : Flow<Resource<Any>> = flow {
        Log.e("READ ALL", list.toString())
        try {
            emit(Resource.Loading<Any>())
            val cookie = db.getCookie()
            val data = api.readNotifications(cookie, list)
            emit(Resource.Success<Any>(data))
        } catch (e: HttpException) {
            Log.e("End Of Season", e.toString())

            val us = db.getLoginAndPassword().username
            val pass = db.getLoginAndPassword().password

            try {
                val response = api.loginToAccount(us, pass).awaitResponse()
                val cookie = response.headers()["Set-Cookie"].toString()
                val answerModel = response.body()?.toModel(cookie)
                db.setLoginAndPassword(LoginAndPasswordModel(username = us, password = pass))
                answerModel?.let { db.setUserBasicData(it) }

                val data = api.readNotifications(cookie, list)
                emit(Resource.Success<Any>(data))
            } catch (e: IOException) {
                if (e.toString() == "java.io.EOFException: End of input at line 1 column 1 path \$") {
                    emit(Resource.Error<Any>("WrongPassword"))
                } else if (e.toString().contains("Unable to resolve host")) {
                    emit(Resource.Error<Any>("ConnectionFailed"))
                } else {
                    emit(Resource.Error<Any>("OtherError"))
                }

            } catch (e: Exception) {
                emit(Resource.Error<Any>("OtherError"))
            }
        } catch (e: IOException) {
            emit(Resource.Error<Any>("ConnectionFailed"))
        } catch (e: Exception) {
            emit(Resource.Error<Any>("OtherError"))
        }
    }
}