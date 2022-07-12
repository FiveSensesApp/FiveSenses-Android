package com.mangpo.domain.usecase

import android.util.Log
import com.mangpo.domain.model.KakaoBookEntiity
import com.mangpo.domain.model.NetworkResult
import com.mangpo.domain.repository.KakaoBookRepository

class GetBooksByNameUseCase (private val repository: KakaoBookRepository): BaseUseCase() {
    suspend fun invoke(name: String): NetworkResult<List<KakaoBookEntiity>> {
//        return repository.getBooksByName(name)
        return networkHandling {
            repository.getBooksByName(name).map {
                Log.d("GetBooksByNameUseCase", "it: $it")
                it
            }
        }
    }
}