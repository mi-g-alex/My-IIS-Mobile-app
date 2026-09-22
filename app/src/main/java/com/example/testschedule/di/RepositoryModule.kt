package com.example.testschedule.di

import com.example.testschedule.data.local.UserDataBase
import com.example.testschedule.data.remote.IisAPI
import com.example.testschedule.data.repository.AccountAwareIisAPIRepository
import com.example.testschedule.data.repository.IisAPIRepositoryImpl
import com.example.testschedule.data.repository.MockIisAPIRepository
import com.example.testschedule.data.repository.UserDatabaseRepositoryImpl
import com.example.testschedule.domain.repository.IisAPIRepository
import com.example.testschedule.domain.repository.UserDatabaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideIisAPIRepository(
        api: IisAPI,
        databaseRepository: UserDatabaseRepository
    ): IisAPIRepository = AccountAwareIisAPIRepository(
        realRepository = IisAPIRepositoryImpl(api),
        mockRepository = MockIisAPIRepository(),
        databaseRepository = databaseRepository
    )

    @Provides
    @Singleton
    fun provideUserDatabaseRepository(db: UserDataBase): UserDatabaseRepository =
        UserDatabaseRepositoryImpl(db.userDao)
}
