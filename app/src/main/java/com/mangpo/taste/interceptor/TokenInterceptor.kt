package com.mangpo.taste.interceptor

import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.reissue.ReissueReqEntity
import com.mangpo.domain.model.reissue.ReissueResEntity
import com.mangpo.domain.repository.AuthRepository
import com.mangpo.taste.util.SpfUtils
import kotlinx.coroutines.*
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor(private val authRepository: AuthRepository): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken: String = SpfUtils.getStrEncryptedSpf("jwt")?: ""
        val request = chain.request().putTokenHeader(accessToken)
        var response: Response = chain.proceed(request)

        if (response.code ==401) {
            synchronized(this) {
                val newAccessToken: String = SpfUtils.getStrEncryptedSpf("jwt")?: ""
                if (accessToken==newAccessToken) {  //같으면 reissue API 호출.
                    val refreshToken: String = SpfUtils.getStrEncryptedSpf("refreshToken")?: ""
                    val reissueResEntity: BaseResEntity<ReissueResEntity?> = runBlocking {
                        authRepository.reissue(ReissueReqEntity(accessToken = accessToken, refreshToken = refreshToken))
                    }

                    return if (reissueResEntity.data!=null) {
                        SpfUtils.writeEncryptedSpf("jwt", reissueResEntity.data!!.accessToken)
                        SpfUtils.writeEncryptedSpf("refreshToken", reissueResEntity.data!!.refreshToken)

                        response.close()
                        chain.proceed(chain.request().putTokenHeader(reissueResEntity.data!!.accessToken))
                    } else {
                        response
                    }
                } else {    //다르면 방금 전에 reissue API 가 호출된 상태. newAccessToken 으로 현재 API 재호출.
                    response.close()
                    return chain.proceed(chain.request().putTokenHeader(newAccessToken))
                }
            }
        } else {
            return response
        }
    }

    private fun Request.putTokenHeader(accessToken: String): Request {
        return this.newBuilder()
            .addHeader(AUTHORIZATION, "Bearer $accessToken")
            .build()
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
    }
}