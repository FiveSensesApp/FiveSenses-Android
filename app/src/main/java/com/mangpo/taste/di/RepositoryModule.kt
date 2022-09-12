package com.mangpo.taste.di

import com.mangpo.data.repository.AuthRepositoryImpl
import com.mangpo.data.repository.PostRepositoryImpl
import com.mangpo.data.repository.UserRepositoryImpl
import com.mangpo.domain.repository.AuthRepository
import com.mangpo.domain.repository.PostRepository
import com.mangpo.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindPostRepository(postRepositoryImpl: PostRepositoryImpl): PostRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}