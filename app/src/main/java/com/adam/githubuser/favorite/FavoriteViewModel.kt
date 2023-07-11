package com.adam.githubuser.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.adam.githubuser.database.FavoriteDatabase
import com.adam.githubuser.database.FavoriteUserDao
import com.adam.githubuser.model.FavoriteUser

class FavoriteViewModel(application: Application): AndroidViewModel(application) {

    private var favDao: FavoriteUserDao?
    private var favDb: FavoriteDatabase?

    init {
        favDb = FavoriteDatabase.getDatabase(application)
        favDao = favDb?.favoriteUserDao()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>>? {
        return favDao?.getFavoriteuser()
    }

}