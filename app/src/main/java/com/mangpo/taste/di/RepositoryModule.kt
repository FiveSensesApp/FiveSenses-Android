package com.mangpo.taste.di

import com.mangpo.data.repository.*
import com.mangpo.domain.repository.*
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

    @Binds
    @Singleton
    abstract fun bindAdminRepository(adminRepositoryImpl: AdminRepositoryImpl): AdminRepository

    @Binds
    @Singleton
    abstract fun bindStatRepository(statRepositoryImpl: StatRepositoryImpl): StatRepository

    @Binds
    @Singleton
    abstract fun bindBadgeRepository(badgeRepositoryImpl: BadgeRepositoryImpl): BadgeRepository
}