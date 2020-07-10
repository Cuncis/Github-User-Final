package com.cuncisboss.consumerapp.data.remote

import com.cuncisboss.consumerapp.BuildConfig
import com.cuncisboss.consumerapp.data.model.DetailUserResponse
import com.cuncisboss.consumerapp.data.model.FollModel
import com.cuncisboss.consumerapp.data.model.SearchUserResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface TheGithubApi {

    @GET("/search/users")
    @Headers("Authorization: ${BuildConfig.GITHUB_TOKEN}")
    suspend fun searchUser(
        @Query("q") username: String
    ): SearchUserResponse

    @GET("/users/{username}")
    @Headers("Authorization: ${BuildConfig.GITHUB_TOKEN}")
    suspend fun getDetailUser(
        @Path("username") username: String
    ): DetailUserResponse

    @GET("/users/{username}/followers")
    @Headers("Authorization: ${BuildConfig.GITHUB_TOKEN}")
    suspend fun getFollowers(
        @Path("username") username: String
    ): List<FollModel>

    @GET("/users/{username}/following")
    @Headers("Authorization: ${BuildConfig.GITHUB_TOKEN}")
    suspend fun getFollowing(
        @Path("username") username: String
    ): List<FollModel>

}