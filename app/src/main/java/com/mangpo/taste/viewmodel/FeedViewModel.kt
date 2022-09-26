package com.mangpo.taste.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.domain.model.getPosts.GetPostsResEntity
import com.mangpo.domain.model.updatePost.UpdatePostReqEntity
import com.mangpo.domain.model.updatePost.UpdatePostResEntity
import com.mangpo.domain.usecase.*
import com.mangpo.taste.base.BaseViewModel
import com.mangpo.taste.base.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val getUserInfoUseCase: GetUserInfoUseCase, private val getPostsUseCase: GetPostsUseCase, private val deletePostUseCase: DeletePostUseCase, private val updatePostUseCase: UpdatePostUseCase, private val findCountByParamUseCase: FindCountByParamUseCase): BaseViewModel() {
    private val _posts: MutableLiveData<Event<GetPostsResEntity>> = MutableLiveData()
    val posts: LiveData<Event<GetPostsResEntity>> get() = _posts

    private val _deletePostResult: MutableLiveData<Int> = MutableLiveData()
    val deletePostResult: LiveData<Int> get() = _deletePostResult

    private val _feedCnt: MutableLiveData<Event<Int?>> = MutableLiveData()
    val feedCnt: LiveData<Event<Int?>> get() = _feedCnt

    fun getPosts(userId: Int, page: Int, sort: String, createDate: String?, star: Int?, category: String?) {
        callApi(
            { getPostsUseCase.invoke(userId, page, sort, createDate, star, category) },
            {
                when (it.code) {
                    200 -> _posts.postValue(Event(it.data!!))
                }
            },
            true
        )
    }

    fun deletePost(postId: Int) {
        callApi(
            { deletePostUseCase.invoke(postId) },
            { _deletePostResult.postValue(it.code) },
            true
        )
    }


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

    fun findCountByParam(userId: Int, category: String?, star: Int?, createdDate: String?) {
        callApi(
            { findCountByParamUseCase.invoke(userId, category, star, createdDate) },
            {
                _feedCnt.postValue(Event(it.data))
            },
            false
        )
    }

    fun getUpdatedPost(): UpdatePostResEntity? = this.updatedPost
}