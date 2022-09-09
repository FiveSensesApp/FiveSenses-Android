package com.mangpo.taste.di

import com.mangpo.data.datasource.AuthRemoteDataSource
import com.mangpo.data.datasource.AuthRemoteDataSourceImpl
import com.mangpo.data.datasource.KakaoBookRemoteDataSource
import com.mangpo.data.datasource.KakaoBookRemoteSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindKakaoBookRemoteSource(kakaoBookRemoteSourceImpl: KakaoBookRemoteSourceImpl): KakaoBookRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(authRemoteDataSourceImpl: AuthRemoteDataSourceImpl): AuthRemoteDataSource
}