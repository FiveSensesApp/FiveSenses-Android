package com.mangpo.taste.di

import com.mangpo.data.repository.KakaoBookRepositoryImpl
import com.mangpo.domain.repository.KakaoBookRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindKakaoBookRepository(kakaoBookRepositoryImpl: KakaoBookRepositoryImpl): KakaoBookRepository
}