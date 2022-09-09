package com.mangpo.taste.di

import com.mangpo.data.repository.AuthRepositoryImpl
import com.mangpo.data.repository.KakaoBookRepositoryImpl
import com.mangpo.domain.repository.AuthRepository
import com.mangpo.domain.repository.KakaoBookRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindKakaoBookRepository(kakaoBookRepositoryImpl: KakaoBookRepositoryImpl): KakaoBookRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
}