package com.adam.githubuser.model

import com.squareup.moshi.Json

data class User(
    @field:Json(name = "login")
    val login: String,

    @field:Json(name = "id")
    val id: Int,

    @field:Json(name = "avatarUrl")
    val avatar_url: String,
)
