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
        val request = chain.request().putTokenHeader(SpfUtils.getStrEncryptedSpf("jwt")?: "")
        var response: Response = chain.proceed(request)

        if (response.code()==401 && response.body().toString().contains("유효한 액세스 토큰이 없습니다.")) {
            val accessToken: String = SpfUtils.getStrEncryptedSpf("jwt")?: ""
            val refreshToken: String = SpfUtils.getStrEncryptedSpf("refreshToken")?: ""

            runBlocking {
                val reissueResEntity: BaseResEntity<ReissueResEntity?> = authRepository.reissue(ReissueReqEntity(accessToken = accessToken, refreshToken = refreshToken))

                if (reissueResEntity.data!=null) {
                    SpfUtils.writeEncryptedSpf("jwt", reissueResEntity.data!!.accessToken)
                    SpfUtils.writeEncryptedSpf("refreshToken", reissueResEntity.data!!.refreshToken)

                    val refreshRequest = chain.request().putTokenHeader(SpfUtils.getStrEncryptedSpf("jwt")?: "")
                    response = chain.proceed(refreshRequest)
                }
            }

            return response
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