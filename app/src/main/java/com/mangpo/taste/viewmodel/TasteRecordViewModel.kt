package com.mangpo.taste.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.domain.model.createPost.CreatePostReqEntity
import com.mangpo.domain.usecase.CreatePostUseCase
import com.mangpo.taste.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TasteRecordViewModel @Inject constructor(private val createPostUseCase: CreatePostUseCase): BaseViewModel() {
    private val _createPostResult: MutableLiveData<Boolean> = MutableLiveData()
    val createPostResult: LiveData<Boolean> get() = _createPostResult

    fun createPost(createPostReqEntity: CreatePostReqEntity) {
        callApi(
            { createPostUseCase.invoke(createPostReqEntity) },
            {
                when (it.code) {
                    201 -> _createPostResult.postValue(true)
                    else -> _createPostResult.postValue(false)
                }
            },
            true
        )
    }
}