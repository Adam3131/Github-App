package com.adam.githubuser.api

import com.adam.githubuser.model.UserResponse
import com.adam.githubuser.model.DetailUser
import com.adam.githubuser.model.User
import retrofit2.Call
import retrofit2.http.*

interface ApiService  {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUser(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailUser>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<User>>
}