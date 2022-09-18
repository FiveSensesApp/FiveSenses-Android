package com.mangpo.taste.di

import com.mangpo.domain.repository.AuthRepository
import com.mangpo.domain.repository.PostRepository
import com.mangpo.domain.repository.UserRepository
import com.mangpo.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {
    @Provides
    fun provideAuthorizeUseCase (authRepository: AuthRepository): AuthorizeUseCase {
        return AuthorizeUseCase(authRepository)
    }

    @Provides
    fun provideGetPostsUseCase (postRepository: PostRepository): GetPostsUseCase {
        return GetPostsUseCase(postRepository)
    }

    @Provides
    fun provideGetUserInfoUseCase (userRepository: UserRepository): GetUserInfoUseCase {
        return GetUserInfoUseCase(userRepository)
    }

    @Provides
    fun provideValidateEmailUseCase (userRepository: UserRepository): ValidateEmailUseCase {
        return ValidateEmailUseCase(userRepository)
    }

    @Provides
    fun provideValidateEmailSendCodeUseCase (userRepository: UserRepository): ValidateEmailSendCodeUseCase {
        return ValidateEmailSendCodeUseCase(userRepository)
    }

    @Provides
    fun provideCreateUserUseCase (authRepository: AuthRepository): CreateUserUseCase {
        return CreateUserUseCase(authRepository)
    }

    @Provides
    fun provideValidateDuplicateUseCase (userRepository: UserRepository): ValidateDuplicateUseCase {
        return ValidateDuplicateUseCase(userRepository)
    }

    @Provides
    fun provideCreatePostUseCase (postRepository: PostRepository): CreatePostUseCase {
        return CreatePostUseCase(postRepository)
    }

    @Provides
    fun provideLostPasswordUseCase (userRepository: UserRepository): LostPasswordUseCase {
        return LostPasswordUseCase(userRepository)
    }
}