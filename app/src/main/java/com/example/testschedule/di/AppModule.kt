package com.example.testschedule.di

import com.example.testschedule.common.Constants
import com.example.testschedule.data.remote.IisAPI
import com.example.testschedule.data.repository.IisAPIRepositoryImpl
import com.example.testschedule.domain.repository.IisAPIRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideIisAPI(): IisAPI {

        val inspector = HttpLoggingInterceptor()
        inspector.level = HttpLoggingInterceptor.Level.BODY

        val clint = OkHttpClient.Builder()
            .addInterceptor(inspector)
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_IIS_API)
            .addConverterFactory(GsonConverterFactory.create())
            .client(clint)
            .build()
            .create(IisAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideIisAPIRepository(api: IisAPI) : IisAPIRepository = IisAPIRepositoryImpl(api)

}