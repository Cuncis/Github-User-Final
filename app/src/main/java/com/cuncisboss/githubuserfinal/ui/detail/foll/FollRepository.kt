package com.cuncisboss.githubuserfinal.ui.detail.foll

import androidx.lifecycle.MutableLiveData
import com.cuncisboss.githubuserfinal.data.remote.ApiClient
import com.cuncisboss.githubuserfinal.data.model.FollModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollRepository {

    val followerList = MutableLiveData<List<FollModel>>()
    val followingList = MutableLiveData<List<FollModel>>()
    val loading = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()

    fun getFollower(username: String): MutableLiveData<List<FollModel>> {
        ApiClient.theGithubApi.getFollowers(username)
            .enqueue(object : Callback<List<FollModel>> {
                override fun onResponse(
                    call: Call<List<FollModel>>,
                    response: Response<List<FollModel>>
                ) {
                    loading.value = false
                    if (response.isSuccessful) {
                        followerList.postValue(response.body())
                    } else {
                        message.value = response.message()
                    }
                }

                override fun onFailure(call: Call<List<FollModel>>, t: Throwable) {
                    loading.value = false
                    message.value = t.message
                }
            })

        return followerList
    }

    fun getFollowing(username: String): MutableLiveData<List<FollModel>> {
        ApiClient.theGithubApi.getFollowing(username)
            .enqueue(object : Callback<List<FollModel>> {
                override fun onResponse(
                    call: Call<List<FollModel>>,
                    response: Response<List<FollModel>>
                ) {
                    loading.value = false
                    if (response.isSuccessful) {
                        followingList.postValue(response.body())
                    } else {
                        message.value = response.message()
                    }
                }

                override fun onFailure(call: Call<List<FollModel>>, t: Throwable) {
                    loading.value = false
                    message.value = t.message
                }
            })

        return followingList
    }

}