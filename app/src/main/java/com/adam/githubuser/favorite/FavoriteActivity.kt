package com.adam.githubuser.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.adam.githubuser.databinding.ActivityFavoriteBinding
import com.adam.githubuser.main.UserAdapter
import com.adam.githubuser.model.FavoriteUser
import com.adam.githubuser.model.User

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: UserAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]

        binding.apply {
            rvFav.setHasFixedSize(true)
            rvFav.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvFav.adapter = adapter
        }

        viewModel.getFavoriteUser()?.observe(this) {
            if (it != null) {
                val list = setFavData(it)
                adapter.setList(list)
            }
        }
    }

    private fun setFavData(favorites: List<FavoriteUser>): ArrayList<User> {
        val listFav = ArrayList<User>()
        for (favorite in favorites) {
            val favMapped = User (favorite.login, favorite.id, favorite.avatar_url)
            listFav.add(favMapped)
        }
        return listFav
    }
}