package com.adam.githubuser.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adam.githubuser.api.ApiConfig
import com.adam.githubuser.database.FavoriteDatabase
import com.adam.githubuser.database.FavoriteUserDao
import com.adam.githubuser.model.FavoriteUser
import com.adam.githubuser.model.DetailUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityDetailViewModel(application: Application) : AndroidViewModel(application) {
    val user = MutableLiveData<DetailUser>()

    private var favDao: FavoriteUserDao?
    private var favDb: FavoriteDatabase?

    init {
        favDb = FavoriteDatabase.getDatabase(application)
        favDao = favDb?.favoriteUserDao()
    }

    fun setUserDetail(username: String?): LiveData<DetailUser> {
        if (username != null) {
            ApiConfig.getApiService().getUserDetail(username)
                .enqueue(object : Callback<DetailUser> {
                    override fun onResponse(
                        call: Call<DetailUser>,
                        response: Response<DetailUser>
                    ) {
                        if (response.isSuccessful) {
                            user.postValue(response.body())
                        }
                    }

                    override fun onFailure(call: Call<DetailUser>, t: Throwable) {
                        Log.d("Failure", t.message.toString())
                    }
                })
        }
        return user
    }

    fun addToFavorite(username: String, id: Int, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var user = FavoriteUser(username, id, avatarUrl)
            favDao?.insert(user)
        }
    }

    suspend fun checkUser(id: Int) = favDao?.checkUser(id)

    fun removeFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            favDao?.delete(id)
        }
    }
}