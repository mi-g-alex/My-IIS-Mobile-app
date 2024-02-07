package com.example.testschedule.domain.use_case.account.study.mark_sheet

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

class CloseMarkSheetUseCase @Inject constructor(
    private val api: IisAPIRepository,
    private val db: UserDatabaseRepository
){

    operator fun invoke(id: Int): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading<Boolean>())
            val cookie = db.getCookie()
            api.closeMarkSheet(id, cookie)
            emit(Resource.Success<Boolean>(true))
        }
        catch (e: HttpException) {

            val us = db.getLoginAndPassword().username
            val pass = db.getLoginAndPassword().password

            try {
                val response = api.loginToAccount(us, pass).awaitResponse()
                val cookie = response.headers()["Set-Cookie"].toString()
                val answerModel = response.body()?.toModel(cookie)
                db.setLoginAndPassword(LoginAndPasswordModel(username = us, password = pass))
                answerModel?.let { db.setUserBasicData(it) }

                api.closeMarkSheet(id, cookie)
                emit(Resource.Success<Boolean>(true))
            } catch (e: IOException) {
                if (e.toString() == "java.io.EOFException: End of input at line 1 column 1 path \$") {
                    emit(Resource.Error<Boolean>("WrongPassword"))
                } else if (e.toString().contains("Unable to resolve host")) {
                    emit(Resource.Error<Boolean>("ConnectionFailed"))
                } else {
                    emit(Resource.Error<Boolean>("OtherError"))
                }

            } catch (e: Exception) {
                emit(Resource.Error<Boolean>("OtherError"))
            }
        } catch (e: IOException) {
            emit(Resource.Error<Boolean>("ConnectionFailed"))
        } catch (e: Exception) {
            emit(Resource.Error<Boolean>("OtherError"))
        }
    }
}