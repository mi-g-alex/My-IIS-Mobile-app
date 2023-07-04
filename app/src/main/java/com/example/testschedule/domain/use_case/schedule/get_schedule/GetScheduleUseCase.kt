package com.example.testschedule.domain.use_case.schedule.get_schedule

import com.example.testschedule.common.Resource
import com.example.testschedule.domain.modal.schedule.ScheduleModel
import com.example.testschedule.domain.repository.IisAPIRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetScheduleUseCase @Inject constructor(
    private val rep: IisAPIRepository
) {
    operator fun invoke(id: String): Flow<Resource<ScheduleModel>> = flow {
        try {
            emit(Resource.Loading())
             val schedule = rep.getSchedule(id)
             emit(Resource.Success(schedule))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "ERROR"))
        } catch (e: IOException) {
            emit(Resource.Error(e.localizedMessage ?: "No internet connection"))
        }
    }
}