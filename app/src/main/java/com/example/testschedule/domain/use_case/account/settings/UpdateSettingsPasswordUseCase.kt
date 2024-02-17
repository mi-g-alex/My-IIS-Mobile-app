package com.example.testschedule.domain.use_case.account.settings

import com.example.testschedule.common.Resource
import com.example.testschedule.data.remote.dto.account.settings.password.ChangePasswordDto
import com.example.testschedule.domain.model.auth.LoginAndPasswordModel
import com.example.testschedule.domain.repository.IisAPIRepository
import com.example.testschedule.domain.repository.UserDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.awaitResponse
import java.io.IOException
import javax.inject.Inject

class UpdateSettingsPasswordUseCase @Inject constructor(
    private val api: IisAPIRepository,
    private val db: UserDatabaseRepository
) {

    operator fun invoke(oldPassword: String, newPassword: String): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading<Boolean>())

            val cookie = db.getCookie()
            val passDto = ChangePasswordDto(oldPassword, newPassword)

            val res = api.settingsUpdatePassword(passDto, cookie).awaitResponse()
            if (res.isSuccessful) {
                val pass = db.getLoginAndPassword().copy(password = newPassword)
                db.setLoginAndPassword(pass)
                emit(Resource.Success(true))
            } else {
                throw HttpException(res)
            }
        } catch (e: HttpException) {
            if (e.code() == 400) {
                emit(Resource.Error<Boolean>("WrongOldPassword"))
                return@flow
            }
            if (e.code() == 401 || e.code() == 403) {
                val us = db.getLoginAndPassword().username
                val pass = db.getLoginAndPassword().password

                try {
                    val response = api.loginToAccount(us, pass).awaitResponse()
                    if (!response.isSuccessful) throw HttpException(response)
                    val cookie = response.headers()["Set-Cookie"].toString()
                    val answerModel = response.body()?.toModel(cookie)
                    db.setLoginAndPassword(LoginAndPasswordModel(username = us, password = pass))
                    answerModel?.let { db.setUserBasicData(it) }

                    val passDto = ChangePasswordDto(oldPassword, newPassword)

                    val res = api.settingsUpdatePassword(passDto, cookie).awaitResponse()

                    if (res.isSuccessful) {
                        val newPass = db.getLoginAndPassword().copy(password = newPassword)
                        db.setLoginAndPassword(newPass)
                        emit(Resource.Success(true))
                    } else throw HttpException(res)
                } catch (e: HttpException) {
                    if (e.code() == 400) {
                        emit(Resource.Error("WrongOldPassword"))
                        return@flow
                    }
                    if (e.code() == 401 || e.code() == 403) {
                        db.deleteUserBasicData()
                        emit(Resource.Error("WrongPassword"))
                        return@flow
                    }
                    emit(Resource.Error("WrongPassword"))
                } catch (e: IOException) {
                    emit(Resource.Error("ConnectionFailed"))
                } catch (e: Exception) {
                    emit(Resource.Error("OtherError"))
                }
            } else {
                emit(Resource.Error("OtherError"))
            }
        } catch (e: IOException) {
            emit(Resource.Error("ConnectionFailed"))
        } catch (e: Exception) {
            emit(Resource.Error("OtherError"))
        }

    }

}