package com.example.testschedule.domain.use_case.account.mark_book

import com.example.testschedule.common.Resource
import com.example.testschedule.domain.model.account.mark_book.MarkBookModel
import com.example.testschedule.domain.repository.IisAPIRepository
import com.example.testschedule.domain.repository.UserDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.awaitResponse
import java.io.IOException
import javax.inject.Inject

class GetMarkBookUseCase @Inject constructor(
    private val api: IisAPIRepository,
    private val db: UserDatabaseRepository,
) {
    operator fun invoke(): Flow<Resource<MarkBookModel>> = flow {
        try {
            emit(Resource.Loading())
            val cookie = db.getCookie()
            val data = api.getMarkBook(cookie)
            db.setMarkBook(data)
            emit(Resource.Success(data))
        } catch (e: HttpException) {
            if (e.code() == 403) {
                val us = db.getLoginAndPassword().username
                val pass = db.getLoginAndPassword().password
                try {
                    val response = api.loginToAccount(us, pass).awaitResponse()
                    if (!response.isSuccessful) throw HttpException(response)
                    val cookie = response.headers()["Set-Cookie"].toString()
                    response.body()?.toModel(cookie)?.let { db.setUserBasicData(it) }
                    val data = api.getMarkBook(cookie)
                    db.setMarkBook(data)
                    emit(Resource.Success(data))
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