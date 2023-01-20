package com.mangpo.taste.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mangpo.taste.interceptor.TokenInterceptor
import com.mangpo.data.service.*
import com.mangpo.domain.repository.AuthRepository
import com.mangpo.taste.BuildConfig
import com.mangpo.taste.util.SpfUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class InterceptorOkHttpClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NoInterceptorOkHttpClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class InterceptorRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NoInterceptorRetrofit

    @Provides
    @Singleton
    fun getInterceptor(): Interceptor {
        return Interceptor {
            val request = it.request().newBuilder().addHeader("Authorization", "Bearer ${SpfUtils.getStrEncryptedSpf("jwt")}")
            val actualRequest = request.build()
            it.proceed(actualRequest)
        }
    }

    @Provides
    @Singleton
    fun getTokenInterceptor(authRepository: AuthRepository): TokenInterceptor {
        return TokenInterceptor(authRepository)
    }

    @NoInterceptorOkHttpClient
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30000, TimeUnit.MILLISECONDS)
            .connectTimeout(30000, TimeUnit.MILLISECONDS)
            .build()
    }

    @InterceptorOkHttpClient
    @Provides
    @Singleton
    fun provideInterceptorOkHttpClient(interceptor: Interceptor, tokenInterceptor: TokenInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30000, TimeUnit.MILLISECONDS)
            .connectTimeout(30000, TimeUnit.MILLISECONDS)
            /*.addInterceptor(interceptor)*/
            .addInterceptor(tokenInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .serializeNulls()
            .serializeSpecialFloatingPointValues()
            .setLenient()
            .create()
    }

    @NoInterceptorRetrofit
    @Provides
    @Singleton
    fun provideRetrofit(@NoInterceptorOkHttpClient client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASEURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @InterceptorRetrofit
    @Provides
    @Singleton
    fun provideInterceptorRetrofit(@InterceptorOkHttpClient client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASEURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthService(@NoInterceptorRetrofit retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun providePostService(@InterceptorRetrofit retrofit: Retrofit): PostService {
        return retrofit.create(PostService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserService(@InterceptorRetrofit retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideNoInterceptorUserService(@NoInterceptorRetrofit retrofit: Retrofit): NoInterceptorUserService {
        return retrofit.create(NoInterceptorUserService::class.java)
    }

    @Provides
    @Singleton
    fun provideAdminService(@InterceptorRetrofit retrofit: Retrofit): AdminService {
        return retrofit.create(AdminService::class.java)
    }

    @Provides
    @Singleton
    fun provideStatService(@InterceptorRetrofit retrofit: Retrofit): StatService {
        return retrofit.create(StatService::class.java)
    }

    @Provides
    @Singleton
    fun provideBadgeService(@InterceptorRetrofit retrofit: Retrofit): BadgeService {
        return retrofit.create(BadgeService::class.java)
    }
}