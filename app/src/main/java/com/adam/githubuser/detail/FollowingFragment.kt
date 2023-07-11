package com.adam.githubuser.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.adam.githubuser.R
import com.adam.githubuser.databinding.FragmentFollowingBinding
import com.adam.githubuser.main.UserAdapter


class FollowingFragment : Fragment(R.layout.fragment_following) {
    private var _binding : FragmentFollowingBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: FollowingViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        username = args?.getString(ActivityDetail.EXTRA_USERNAME).toString()
        _binding = FragmentFollowingBinding.bind(view)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding?.apply {
            rvFollowing.setHasFixedSize(true)
            rvFollowing.layoutManager = LinearLayoutManager(activity)
            rvFollowing.adapter = adapter
        }
        showLoading(true)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FollowingViewModel::class.java]
        viewModel.setListFollowing(username)
        viewModel.getListFollowing().observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding?.progressBar?.visibility = View.INVISIBLE
                binding?.tvNoFollowing?.visibility = View.VISIBLE
            } else {
                showLoading(false)
                adapter.setList(it)
                binding?.tvNoFollowing?.visibility = View.INVISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.rvFollowing?.visibility = View.INVISIBLE
            binding?.tvNoFollowing?.visibility = View.INVISIBLE
            binding?.progressBar?.visibility = View.VISIBLE
        } else {
            binding?.tvNoFollowing?.visibility = View.VISIBLE
            binding?.progressBar?.visibility = View.GONE
            binding?.rvFollowing?.visibility = View.VISIBLE
        }
    }
}