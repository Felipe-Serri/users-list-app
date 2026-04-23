// ui/UserAdapter.kt
package com.felipeserri.userlistapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.felipeserri.userlistapp.databinding.ItemUserBinding
import com.felipeserri.userlistapp.model.User

class UserAdapter(
    private val users: List<User>,
    private val onUserClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(private val binding: ItemUserBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.tvName.text  = user.name
            binding.tvEmail.text = user.email
            // ✅ Inicial do nome no avatar
            binding.tvAvatar.text = user.name.first().uppercaseChar().toString()
            binding.root.setOnClickListener { onUserClick(user) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size
}