package com.cuncisboss.githubuserfinal.ui.search

import androidx.lifecycle.MutableLiveData
import com.cuncisboss.githubuserfinal.data.remote.ApiClient
import com.cuncisboss.githubuserfinal.data.model.SearchUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchUserRepository {

    val users = MutableLiveData<SearchUserResponse>()
    val loading = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()

    fun searchUser(username: String): MutableLiveData<SearchUserResponse> {
        loading.value = true
        ApiClient.theGithubApi.searchUser(username)
            .enqueue(object : Callback<SearchUserResponse> {
                override fun onResponse(call: Call<SearchUserResponse>, response: Response<SearchUserResponse>) {
                    loading.value = false
                    if (response.isSuccessful) {
                        users.postValue(response.body())
                    } else {
                        message.value = response.message()
                    }
                }
                override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                    loading.value = false
                    message.value = t.message
                }
            })

        return users
    }
}