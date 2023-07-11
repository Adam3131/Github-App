package com.adam.githubuser.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.adam.githubuser.R
import com.adam.githubuser.databinding.FragmentFollowersBinding
import com.adam.githubuser.main.UserAdapter


class FollowersFragment : Fragment(R.layout.fragment_followers) {
    private var _binding : FragmentFollowersBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: FollowersViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        username = args?.getString(ActivityDetail.EXTRA_USERNAME).toString()
        _binding = FragmentFollowersBinding.bind(view)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding?.apply {
            rvFollowers.setHasFixedSize(true)
            rvFollowers.layoutManager = LinearLayoutManager(activity)
            rvFollowers.adapter = adapter
        }
        showLoading(true)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FollowersViewModel::class.java]
        viewModel.setListFollowers(username)
        viewModel.getListFollowers().observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding?.progressBar?.visibility = View.INVISIBLE
                binding?.tvNoFollowers?.visibility = View.VISIBLE
            } else {
                showLoading(false)
                adapter.setList(it)
                binding?.tvNoFollowers?.visibility = View.INVISIBLE
           }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.tvNoFollowers?.visibility = View.INVISIBLE
            binding?.progressBar?.visibility = View.VISIBLE
            binding?.rvFollowers?.visibility = View.INVISIBLE
        } else {
            binding?.tvNoFollowers?.visibility = View.VISIBLE
            binding?.progressBar?.visibility = View.GONE
            binding?.rvFollowers?.visibility = View.VISIBLE
        }
    }
}