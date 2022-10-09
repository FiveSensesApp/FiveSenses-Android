package com.mangpo.taste.di

import com.mangpo.data.datasource.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(authRemoteDataSourceImpl: AuthRemoteDataSourceImpl): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindPostRemoteDataSource(postRemoteDataSourceImpl: PostRemoteDataSourceImpl): PostRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindUserRemoteDataSource(userRemoteDataSourceImpl: UserRemoteDataSourceImpl): UserRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAdminRemoteDataSource(adminRemoteDataSourceImpl: AdminRemoteDataSourceImpl): AdminRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindStatRemoteDataSource(statRemoteDataSourceImpl: StatRemoteDataSourceImpl): StatRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindBadgeRemoteDataSource(badgeRemoteDataSourceImpl: BadgeRemoteDataSourceImpl): BadgeRemoteDataSource
}