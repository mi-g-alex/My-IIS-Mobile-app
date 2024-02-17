package com.example.testschedule.domain.use_case.account.settings

import com.example.testschedule.common.Resource
import com.example.testschedule.domain.repository.IisAPIRepository
import com.example.testschedule.domain.repository.UserDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.awaitResponse
import java.io.IOException
import javax.inject.Inject

class UpdateSettingsViewUseCase @Inject constructor(
    private val api: IisAPIRepository,
    private val db: UserDatabaseRepository
) {

    operator fun invoke(
        viewProfile: Boolean? = null,
        viewRating: Boolean? = null,
        viewJob: Boolean? = null
    ): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())

            val cookie = db.getCookie()
            val data = db.getAccountProfile()
            var ans = false
            when {
                viewProfile != null -> {
                    if (data != null) {
                        api.settingsUpdateViewProfile(
                            data.copy(settingPublished = viewProfile),
                            cookie
                        )
                        db.setAccountProfile(data.copy(settingPublished = viewProfile))
                    }
                    ans = viewProfile
                }

                viewRating != null -> {
                    if (data != null) {
                        api.settingsUpdateViewRating(
                            data.copy(settingShowRating = viewRating),
                            cookie
                        )
                        db.setAccountProfile(data.copy(settingShowRating = viewRating))

                    }
                    ans = viewRating
                }

                viewJob != null -> {
                    if (data != null) {
                        api.settingsUpdateViewJob(
                            data.copy(settingSearchJob = viewJob),
                            cookie
                        )
                        db.setAccountProfile(data.copy(settingSearchJob = viewJob))
                    }
                    ans = viewJob
                }
            }
            emit(Resource.Success(ans))
        } catch (e: HttpException) {
            if (e.code() == 403) {
                val us = db.getLoginAndPassword().username
                val pass = db.getLoginAndPassword().password
                try {
                    val response = api.loginToAccount(us, pass).awaitResponse()
                    if (!response.isSuccessful) throw HttpException(response)
                    val cookie = response.headers()["Set-Cookie"].toString()
                    response.body()?.toModel(cookie)?.let { db.setUserBasicData(it) }
                    val data = db.getAccountProfile()
                    var ans = false
                    when {
                        viewProfile != null -> {
                            if (data != null) {
                                api.settingsUpdateViewProfile(
                                    data.copy(settingPublished = viewProfile),
                                    cookie
                                )
                                db.setAccountProfile(data.copy(settingPublished = viewProfile))
                            }
                            ans = viewProfile
                        }

                        viewRating != null -> {
                            if (data != null) {
                                api.settingsUpdateViewRating(
                                    data.copy(settingShowRating = viewRating),
                                    cookie
                                )
                                db.setAccountProfile(data.copy(settingShowRating = viewRating))

                            }
                            ans = viewRating
                        }

                        viewJob != null -> {
                            if (data != null) {
                                api.settingsUpdateViewJob(
                                    data.copy(settingSearchJob = viewJob),
                                    cookie
                                )
                                db.setAccountProfile(data.copy(settingSearchJob = viewJob))
                            }
                            ans = viewJob
                        }
                    }
                    emit(Resource.Success(ans))
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