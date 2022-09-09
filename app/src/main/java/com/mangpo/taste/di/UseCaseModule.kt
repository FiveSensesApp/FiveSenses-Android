package com.mangpo.taste.di

import com.mangpo.domain.repository.AuthRepository
import com.mangpo.domain.repository.KakaoBookRepository
import com.mangpo.domain.usecase.AuthorizeUseCase
import com.mangpo.domain.usecase.GetBooksByNameUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {
    @Provides
    fun providesGetBooksByNameUseCase(kakaoBookRepository: KakaoBookRepository): GetBooksByNameUseCase {
        return GetBooksByNameUseCase(kakaoBookRepository)
    }

    @Provides
    fun provideAuthorizeUseCase (authRepository: AuthRepository): AuthorizeUseCase {
        return AuthorizeUseCase(authRepository)
    }
}