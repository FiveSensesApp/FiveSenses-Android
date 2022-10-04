package com.mangpo.taste.di

import com.mangpo.domain.repository.*
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

    @Provides
    fun provideDeletePostUseCase (postRepository: PostRepository): DeletePostUseCase {
        return DeletePostUseCase(postRepository)
    }

    @Provides
    fun provideUpdatePostUseCase (postRepository: PostRepository): UpdatePostUseCase {
        return UpdatePostUseCase(postRepository)
    }

    @Provides
    fun provideFindCountByParamUseCase (postRepository: PostRepository): FindCountByParamUseCase {
        return FindCountByParamUseCase(postRepository)
    }

    @Provides
    fun provideGetPresentPostsBetweenUseCase (postRepository: PostRepository): GetPresentPostsBetweenUseCase {
        return GetPresentPostsBetweenUseCase(postRepository)
    }

    @Provides
    fun provideDeleteUserUseCase (adminRepository: AdminRepository): DeleteUserUseCase {
        return DeleteUserUseCase(adminRepository)
    }

    @Provides
    fun provideUpdateUserUseCase (userRepository: UserRepository): UpdateUserUseCase {
        return UpdateUserUseCase(userRepository)
    }

    @Provides
    fun provideGetStatUseCase (statRepository: StatRepository): GetStatUseCase {
        return GetStatUseCase(statRepository)
    }
}