package com.mangpo.taste.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.domain.model.getPosts.GetPostsResEntity
import com.mangpo.domain.usecase.GetPostsUseCase
import com.mangpo.domain.usecase.GetUserInfoUseCase
import com.mangpo.taste.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val getUserInfoUseCase: GetUserInfoUseCase, private val getPostsUseCase: GetPostsUseCase): BaseViewModel() {
    private val _posts: MutableLiveData<GetPostsResEntity> = MutableLiveData()
    val posts: LiveData<GetPostsResEntity> get() = _posts

    fun getPosts(userId: Int, page: Int, sort: String, createDate: String?, star: Int?, category: String?) {
        callApi(
            { getPostsUseCase.invoke(userId, page, sort, createDate, star, category) },
            {
                when (it.code) {
                    200 -> _posts.postValue(it.data)
                }
            },
            true
        )
    }
}