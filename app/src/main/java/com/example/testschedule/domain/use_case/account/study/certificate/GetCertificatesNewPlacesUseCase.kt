package com.example.testschedule.domain.use_case.account.study.certificate

import com.example.testschedule.common.Resource
import com.example.testschedule.domain.model.account.study.certificate.NewCertificatePlacesModel
import com.example.testschedule.domain.repository.IisAPIRepository
import com.example.testschedule.domain.repository.UserDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCertificatesNewPlacesUseCase @Inject constructor(
    private val api: IisAPIRepository,
    private val db: UserDatabaseRepository
) {

    operator fun invoke(): Flow<Resource<List<NewCertificatePlacesModel>>> = flow {
        try {
            emit(Resource.Loading<List<NewCertificatePlacesModel>>())

            val data = api.getNewCertificatePlaces()
            db.deleteCertificatesPlaces()
            db.addCertificatePlaces(data)

            emit(Resource.Success<List<NewCertificatePlacesModel>>(data))
        } catch (e: HttpException) {
            emit(Resource.Error<List<NewCertificatePlacesModel>>("HttpError"))
        } catch (e: IOException) {
            emit(Resource.Error<List<NewCertificatePlacesModel>>("ConnectionFailed"))
        } catch (e: Exception) {
            emit(Resource.Error<List<NewCertificatePlacesModel>>("OtherError"))
        }

    }

}