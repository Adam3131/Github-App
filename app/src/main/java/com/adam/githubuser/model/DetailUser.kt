package com.adam.githubuser.model

import com.squareup.moshi.Json

data class DetailUser(
    @field:Json(name = "login")
    val login: String,

    @field:Json(name = "id")
    val id: Int,

    @field:Json(name = "avatarUrl")
    val avatar_url: String,

    @field:Json(name = "followersUrl")
    val followers_url: String,

    @field:Json(name = "followingUrl")
    val following_url: String,

    @field:Json(name = "name")
    val name: String,

    @field:Json(name = "following")
    val following: Int,

    @field:Json(name = "followers")
    val followers: Int
)