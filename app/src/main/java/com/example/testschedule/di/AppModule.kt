package com.example.testschedule.di

import android.app.Application
import androidx.room.Room
import com.example.testschedule.common.Constants
import com.example.testschedule.data.local.Converters
import com.example.testschedule.data.local.UserDataBase
import com.example.testschedule.data.remote.IisAPI
import com.example.testschedule.data.repository.IisAPIRepositoryImpl
import com.example.testschedule.data.repository.UserDatabaseRepositoryImpl
import com.example.testschedule.data.util.GsonParser
import com.example.testschedule.domain.repository.IisAPIRepository
import com.example.testschedule.domain.repository.UserDatabaseRepository
import com.google.gson.Gson
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
    fun provideIisAPIRepository(api: IisAPI): IisAPIRepository = IisAPIRepositoryImpl(api)


    @Provides
    @Singleton
    fun provideUserDatabase(app: Application): UserDataBase =
        Room.databaseBuilder(
            app,
            UserDataBase::class.java,
            UserDataBase.DATABASE_NAME
        )
            .addTypeConverter(Converters(GsonParser(Gson())))
            .build()


    @Provides
    @Singleton
    fun provideUserDataBaseRepository(db : UserDataBase) : UserDatabaseRepository = UserDatabaseRepositoryImpl(db.userDao)

}