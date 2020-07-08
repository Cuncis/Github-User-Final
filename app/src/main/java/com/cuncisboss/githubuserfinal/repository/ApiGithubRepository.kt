package com.cuncisboss.githubuserfinal.repository

import com.cuncisboss.githubuserfinal.data.model.DetailUserResponse
import com.cuncisboss.githubuserfinal.data.model.FollModel
import com.cuncisboss.githubuserfinal.data.model.SearchUserResponse
import com.cuncisboss.githubuserfinal.data.remote.TheGithubApi

class ApiGithubRepository(private val apiService: TheGithubApi) {

    suspend fun getSearchUser(username: String): SearchUserResponse {
        return apiService.searchUser(username)
    }

    suspend fun getDetailUser(username: String): DetailUserResponse {
        return apiService.getDetailUser(username)
    }

    suspend fun getFollower(username: String): List<FollModel> {
        return apiService.getFollowers(username)
    }

    suspend fun getFollowing(username: String): List<FollModel> {
        return apiService.getFollowing(username)
    }
}