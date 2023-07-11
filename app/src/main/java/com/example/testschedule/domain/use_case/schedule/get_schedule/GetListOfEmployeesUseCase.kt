package com.example.testschedule.domain.use_case.schedule.get_schedule

import com.example.testschedule.common.Resource
import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel
import com.example.testschedule.domain.repository.IisAPIRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetListOfEmployeesUseCase @Inject constructor(
    private val rep: IisAPIRepository
){
    operator fun invoke() : Flow<Resource<List<ListOfEmployeesModel>>> = flow {
        try {
            emit(Resource.Loading())
            val list = rep.getListOfEmployees()
            emit(Resource.Success(list))
        } catch (e: IOException) {
            emit(Resource.Error(e.localizedMessage ?: "No internet connection"))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "ERROR"))
        }
    }
}