package com.cuncisboss.githubuserfinal.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cuncisboss.githubuserfinal.data.model.DetailUserResponse

class DetailUserViewModel : ViewModel() {
    private val repository = DetailUserRepository()

    fun getDetailUser(username: String) : MutableLiveData<DetailUserResponse> {
        return repository.getDetailUser(username)
    }

    fun getMessage(): MutableLiveData<String> {
        return repository.message
    }
}