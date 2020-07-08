package com.cuncisboss.githubuserfinal.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cuncisboss.githubuserfinal.data.model.DetailUserResponse

class DetailUserViewModel : ViewModel() {
    private val repository = DetailUserRepository()
    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: MutableLiveData<DetailUserResponse>
        get() = _detailUser

    fun getDetailUser(username: String) : MutableLiveData<DetailUserResponse> {
        return repository.getDetailUser(username)
    }

    fun getMessage(): MutableLiveData<String> {
        return repository.message
    }

    fun setFavorite(username: String) {
        _detailUser.postValue(repository.getDetailUser(username).value)
    }
}