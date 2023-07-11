package com.adam.githubuser.model

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("items")
	val items: List<User>
)