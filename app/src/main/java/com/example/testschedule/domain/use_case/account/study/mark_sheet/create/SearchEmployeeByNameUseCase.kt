package com.example.testschedule.domain.use_case.account.study.mark_sheet.create

import com.example.testschedule.common.Resource
import com.example.testschedule.domain.model.account.study.mark_sheet.create.SearchEmployeeMarkSheetModel
import com.example.testschedule.domain.repository.IisAPIRepository
import com.example.testschedule.domain.repository.UserDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class SearchEmployeeByNameUseCase @Inject constructor(
    private val api: IisAPIRepository,
) {

    operator fun invoke(name: String): Flow<Resource<List<SearchEmployeeMarkSheetModel>>> = flow {
        try {
            emit(Resource.Loading<List<SearchEmployeeMarkSheetModel>>())

            val data = api.searchEmployeeByName(name)

            emit(Resource.Success<List<SearchEmployeeMarkSheetModel>>(data))
        } catch (e: IOException) {
            emit(Resource.Error<List<SearchEmployeeMarkSheetModel>>("ConnectionFailed"))
        } catch (e: Exception) {
            emit(Resource.Error<List<SearchEmployeeMarkSheetModel>>("OtherError"))
        }

    }

}