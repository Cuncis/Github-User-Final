package com.cuncisboss.consumerapp.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuncisboss.consumerapp.data.model.DetailUserResponse
import com.cuncisboss.consumerapp.data.model.FollModel
import com.cuncisboss.consumerapp.data.model.Resource
import com.cuncisboss.consumerapp.data.model.SearchUserResponse
import com.cuncisboss.consumerapp.repository.ApiGithubRepository
import kotlinx.coroutines.launch
import java.lang.Exception


class ApiGithubViewModel(private val repository: ApiGithubRepository) : ViewModel() {

    private val _searchUser = MutableLiveData<Resource<SearchUserResponse>>()
    val searchUser: MutableLiveData<Resource<SearchUserResponse>>
        get() = _searchUser

    private val _detailUser = MutableLiveData<Resource<DetailUserResponse>>()
    val detailUser: MutableLiveData<Resource<DetailUserResponse>>
        get() = _detailUser

    private val _favDetail = MutableLiveData<DetailUserResponse>()
    val favDetail: MutableLiveData<DetailUserResponse>
        get() = _favDetail

    private val _followerList = MutableLiveData<Resource<List<FollModel>>>()
    val followerList: MutableLiveData<Resource<List<FollModel>>>
        get() = _followerList

    private val _followingList = MutableLiveData<Resource<List<FollModel>>>()
    val followingList: MutableLiveData<Resource<List<FollModel>>>
        get() = _followingList

    fun getSearchUser(username: String) {
        _searchUser.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                val response = repository.getSearchUser(username)
                _searchUser.postValue(Resource.success(response))
            } catch (t: Throwable) {
                _searchUser.postValue(Resource.error(t.message.toString(), null, t))
            }
        }
    }

    fun getDetailUser(username: String) {
        _detailUser.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                val response = repository.getDetailUser(username)
                _detailUser.postValue(Resource.success(response))
            } catch (t: Throwable) {
                _detailUser.postValue(Resource.error(t.message.toString(), null, t))
            }
        }
    }

    fun setFavorite(username: String) {
        viewModelScope.launch {
            try {
                _favDetail.postValue(repository.getDetailUser(username))
            } catch (e: Exception) { }
        }
    }

    fun getFollower(username: String) {
        _followerList.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                val response = repository.getFollower(username)
                _followerList.postValue(Resource.success(response))
            } catch (t: Throwable) {
                _followerList.postValue(Resource.error(t.message.toString(), null, t))
            }
        }
    }

    fun getFollowing(username: String) {
        _followingList.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                val response = repository.getFollowing(username)
                _followingList.postValue(Resource.success(response))
            } catch (t: Throwable) {
                _followingList.postValue(Resource.error(t.message.toString(), null, t))
            }
        }
    }

}