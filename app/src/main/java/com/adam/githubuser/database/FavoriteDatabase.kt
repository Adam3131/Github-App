package com.adam.githubuser.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.adam.githubuser.model.FavoriteUser

@Database(entities = [FavoriteUser::class], version = 1)
abstract class FavoriteDatabase: RoomDatabase() {

    abstract fun favoriteUserDao(): FavoriteUserDao

    companion object {
        var INSTANCE: FavoriteDatabase? = null

        fun getDatabase(context: Context): FavoriteDatabase? {
            if (INSTANCE == null) {
                synchronized(FavoriteDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, FavoriteDatabase::class.java, "user_database").build()
                }
            }
            return INSTANCE
        }
    }
}