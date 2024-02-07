package com.example.testschedule.domain.use_case.account.study.certificate

import android.util.Log
import com.example.testschedule.common.Resource
import com.example.testschedule.domain.model.account.study.certificate.CreateCertificateModel
import com.example.testschedule.domain.model.auth.LoginAndPasswordModel
import com.example.testschedule.domain.repository.IisAPIRepository
import com.example.testschedule.domain.repository.UserDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.awaitResponse
import java.io.IOException
import javax.inject.Inject

class CreateCertificatesUseCase @Inject constructor(
    private val api: IisAPIRepository,
    private val db: UserDatabaseRepository
) {

    operator fun invoke(
        certificatePlace: String,
        certificateType: String,
        certificateCount: Int,
    ): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading<Boolean>())

            val cookie = db.getCookie()
            val certificate = CreateCertificateModel(
                certificateCount, CreateCertificateModel.CreateCertificateItemModel(
                    certificateType, certificatePlace
                )
            )
            api.createCertificate(certificate, cookie).awaitResponse()

            emit(Resource.Success<Boolean>(true))
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

                val certificate = CreateCertificateModel(
                    certificateCount, CreateCertificateModel.CreateCertificateItemModel(
                        certificateType, certificatePlace
                    )
                )

                api.createCertificate(certificate, cookie).awaitResponse()

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