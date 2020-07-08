package com.cuncisboss.githubuserfinal.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cuncisboss.githubuserfinal.data.model.SearchUserResponse

class SearchUserViewModel : ViewModel() {
    private val repository =
        SearchUserRepository()

    fun searchUser(username: String): MutableLiveData<SearchUserResponse> {
        return repository.searchUser(username)
    }

    fun onLoading(): MutableLiveData<Boolean> {
        return repository.loading
    }

    fun getMessage(): MutableLiveData<String> {
        return repository.message
    }

}