package com.adam.githubuser.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.adam.githubuser.R
import com.adam.githubuser.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityDetail : AppCompatActivity() {
    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    private lateinit var detailBinding: ActivityDetailBinding
    private lateinit var viewModel: ActivityDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        val sentUserName = intent.getStringExtra(EXTRA_USERNAME)
        val sentUserId = intent.getIntExtra(EXTRA_ID, 0)
        val sentUserUrl = intent.getStringExtra(EXTRA_URL)
        val bundle = Bundle()
        showLoading(true)
        bundle.putString(EXTRA_USERNAME, sentUserName)

        viewModel = ViewModelProvider(this)[ActivityDetailViewModel::class.java]
        if (sentUserName != null) {
            viewModel.setUserDetail(sentUserName)
        }
        viewModel.setUserDetail(sentUserName).observe(this) {
            if (it != null) {
                showLoading(false)
                detailBinding.apply {
                    tvName.text = it.name
                    tvUsername.text = it.login
                    tvFollowers.text = resources.getString(R.string.detail_followers, "${it.followers}")
                    tvFollowing.text = resources.getString(R.string.detail_following, "${it.following}")
                }
                Glide.with(this@ActivityDetail)
                    .load(it.avatar_url)
                    .centerCrop()
                    .into(detailBinding.circleImageView)
            }
        }

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(sentUserId)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        detailBinding.toggleFavorite.isChecked = true
                        _isChecked = true
                    } else {
                        detailBinding.toggleFavorite.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }

        detailBinding.toggleFavorite.setOnClickListener {
            _isChecked = !_isChecked
            if (_isChecked) {
                if (sentUserName != null && sentUserUrl != null) {
                    viewModel.addToFavorite(sentUserName, sentUserId, sentUserUrl)
                }
            } else {
                viewModel.removeFromFavorite(sentUserId)
            }
            detailBinding.toggleFavorite.isChecked = _isChecked
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, bundle)
        val viewPager: ViewPager2 = detailBinding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = detailBinding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            detailBinding.progressBar.visibility = View.VISIBLE
        } else {
            detailBinding.circleImageView.visibility = View.VISIBLE
            detailBinding.tvName.visibility = View.VISIBLE
            detailBinding.tvUsername.visibility = View.VISIBLE
            detailBinding.tvFollowers.visibility = View.VISIBLE
            detailBinding.tvFollowing.visibility = View.VISIBLE
            detailBinding.tabs.visibility = View.VISIBLE
            detailBinding.viewPager.visibility = View.VISIBLE
            detailBinding.progressBar.visibility = View.GONE
        }
    }
}