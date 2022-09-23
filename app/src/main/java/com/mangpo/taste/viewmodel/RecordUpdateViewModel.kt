package com.mangpo.taste.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.domain.model.updatePost.UpdatePostReqEntity
import com.mangpo.domain.model.updatePost.UpdatePostResEntity
import com.mangpo.domain.usecase.UpdatePostUseCase
import com.mangpo.taste.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecordUpdateViewModel @Inject constructor(private val updatePostUseCase: UpdatePostUseCase): BaseViewModel() {
    private val _updatePostResCode: MutableLiveData<Int> = MutableLiveData()
    val updatePostResCode: LiveData<Int> get() = _updatePostResCode

    private var updatedPost: UpdatePostResEntity? = null

    fun updatePost(updatePostReqEntity: UpdatePostReqEntity) {
        callApi(
            { updatePostUseCase.invoke(updatePostReqEntity) },
            {
                _updatePostResCode.postValue(it.code)
                updatedPost = it.data

                when (it.code) {
                    200 -> showToast("수정이 완료되었습니다.")
                    404 -> showToast(it.msg!!)
                }
            },
            true
        )
    }

    fun getUpdatedPost(): UpdatePostResEntity? = this.updatedPost
}