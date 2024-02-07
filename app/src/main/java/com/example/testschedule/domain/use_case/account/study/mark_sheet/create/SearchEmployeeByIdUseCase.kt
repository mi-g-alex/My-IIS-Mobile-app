package com.example.testschedule.domain.use_case.account.study.mark_sheet.create

import android.util.Log
import com.example.testschedule.common.Resource
import com.example.testschedule.domain.model.account.study.mark_sheet.create.SearchEmployeeMarkSheetModel
import com.example.testschedule.domain.model.auth.LoginAndPasswordModel
import com.example.testschedule.domain.repository.IisAPIRepository
import com.example.testschedule.domain.repository.UserDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.awaitResponse
import java.io.IOException
import javax.inject.Inject

class SearchEmployeeByIdUseCase @Inject constructor(
    private val api: IisAPIRepository,
    private val db: UserDatabaseRepository
) {

    operator fun invoke(thId: Int?, focsId: Int?): Flow<Resource<List<SearchEmployeeMarkSheetModel>>> = flow {
        try {
            emit(Resource.Loading<List<SearchEmployeeMarkSheetModel>>())

            val cookie = db.getCookie()
            val data = api.searchEmployeeById(thId, focsId, cookie)

            emit(Resource.Success<List<SearchEmployeeMarkSheetModel>>(data))
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

                val data = api.searchEmployeeById(thId, focsId, cookie)
                emit(Resource.Success<List<SearchEmployeeMarkSheetModel>>(data))
            } catch (e: IOException) {
                if (e.toString() == "java.io.EOFException: End of input at line 1 column 1 path \$") {
                    emit(Resource.Error<List<SearchEmployeeMarkSheetModel>>("WrongPassword"))
                } else if (e.toString().contains("Unable to resolve host")) {
                    emit(Resource.Error<List<SearchEmployeeMarkSheetModel>>("ConnectionFailed"))
                } else {
                    emit(Resource.Error<List<SearchEmployeeMarkSheetModel>>("OtherError"))
                }

            } catch (e: Exception) {
                emit(Resource.Error<List<SearchEmployeeMarkSheetModel>>("OtherError"))
            }
        } catch (e: IOException) {
            emit(Resource.Error<List<SearchEmployeeMarkSheetModel>>("ConnectionFailed"))
        } catch (e: Exception) {
            emit(Resource.Error<List<SearchEmployeeMarkSheetModel>>("OtherError"))
        }

    }

}