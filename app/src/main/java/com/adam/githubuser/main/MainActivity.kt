package com.adam.githubuser.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.adam.githubuser.databinding.ActivityMainBinding
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.adam.githubuser.*
import com.adam.githubuser.favorite.FavoriteActivity
import com.adam.githubuser.helper.ViewModelFactory
import com.adam.githubuser.model.User
import com.adam.githubuser.setting.SettingActivity
import com.adam.githubuser.setting.SettingPreferences

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this@MainActivity, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        val pref = SettingPreferences.getInstance(dataStore)
        val myMainViewModel = ViewModelProvider(this, ViewModelFactory(pref))[MainViewModel::class.java]
        myMainViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    mainViewModel.findUser(query).observe(this@MainActivity) { items ->
                        setUserData(items)
                    }
                    mainViewModel.isLoading.observe(this@MainActivity) {
                        showLoading(it)
                    }
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fav_menu -> {
                Intent(this, FavoriteActivity::class.java).also {
                    startActivity(it)
                }
            }
            R.id.settings -> {
                Intent(this, SettingActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUserData(items: List<User>?) {
        val listUser = ArrayList<User>()
        if (items != null) {
            for (userData in items) {
                listUser.addAll(items)
            }
        }
        val listAdapter = UserAdapter()
        binding.rvUser.adapter = listAdapter
        listAdapter.setList(listUser)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.welcomeText.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.welcomeText.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }
}