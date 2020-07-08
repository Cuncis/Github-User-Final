package com.cuncisboss.githubuserfinal.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class UserGithubResponse(

	@SerializedName("users")
	val users: List<UserGithub>
)

@Parcelize
data class UserGithub(

	@SerializedName("follower")
	val follower: Int = 0,

	@SerializedName("following")
	val following: Int = 0,

	@SerializedName("name")
	val name: String = "",

	@SerializedName("company")
	val company: String = "",

	@SerializedName("location")
	val location: String = "",

	@SerializedName("avatar")
	var avatar: String = "",

	@SerializedName("repository")
	val repository: Int = 0,

	@SerializedName("username")
	var username: String = "",

	@SerializedName("login")
	var login: String = "",

	@SerializedName("avatar_url")
	var avatarUrl: String = ""
) : Parcelable