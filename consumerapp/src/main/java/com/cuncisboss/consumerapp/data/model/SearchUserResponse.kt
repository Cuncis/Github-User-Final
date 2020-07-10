package com.cuncisboss.consumerapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchUserResponse(

	@SerializedName("total_count")
	val totalCount: Int,

	@SerializedName("incomplete_results")
	val incompleteResults: Boolean,

	@SerializedName("items")
	val items: List<UserGithub>
) : Parcelable

@Parcelize
data class Items(

	@SerializedName("gists_url")
	val gistsUrl: String = "",

	@SerializedName("repos_url")
	val reposUrl: String = "",

	@SerializedName("following_url")
	val followingUrl: String = "",

	@SerializedName("starred_url")
	val starredUrl: String = "",

	@SerializedName("login")
	var login: String = "",

	@SerializedName("followers_url")
	val followersUrl: String = "",

	@SerializedName("type")
	val type: String = "",

	@SerializedName("url")
	val url: String = "",

	@SerializedName("subscriptions_url")
	val subscriptionsUrl: String = "",

	@SerializedName("score")
	val score: Double = 0.0,

	@SerializedName("received_events_url")
	val receivedEventsUrl: String = "",

	@SerializedName("avatar_url")
	var avatarUrl: String = "",

	@SerializedName("events_url")
	val eventsUrl: String = "",

	@SerializedName("html_url")
	val htmlUrl: String = "",

	@SerializedName("site_admin")
	val siteAdmin: Boolean = false,

	@SerializedName("id")
	val id: Int = 0,

	@SerializedName("gravatar_id")
	val gravatarId: String = "",

	@SerializedName("node_id")
	val nodeId: String = "",

	@SerializedName("organizations_url")
	val organizationsUrl: String = ""
) : Parcelable
