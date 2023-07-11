package com.adam.githubuser.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adam.githubuser.databinding.ItemRowUserBinding
import com.adam.githubuser.detail.ActivityDetail
import com.adam.githubuser.model.User
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private val list = ArrayList<User>()

    companion object {
        private const val EXTRA_USERNAME = "extra_username"
    }

    fun setList(users: ArrayList<User>) {
        list.clear()
        list.addAll(users)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                tvUsername.text = user.login
            }
            Glide.with(itemView)
                .load(user.avatar_url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(binding.ivUser)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ActivityDetail::class.java)
                intent.putExtra(EXTRA_USERNAME, user.login)
                intent.putExtra(ActivityDetail.EXTRA_ID, user.id)
                intent.putExtra(ActivityDetail.EXTRA_URL, user.avatar_url)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
}