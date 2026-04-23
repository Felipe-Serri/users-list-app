package com.felipeserri.userlistapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.felipeserri.userlistapp.databinding.ActivityDetailBinding
import com.felipeserri.userlistapp.model.User
import com.felipeserri.userlistapp.utils.AnalyticsTracker
import com.google.gson.Gson

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userJson = intent.getStringExtra(EXTRA_USER) ?: run {
            finish()
            return
        }
        val user = Gson().fromJson(userJson, User::class.java)
        setupToolbar(user.name)
        bindUserData(user)
        AnalyticsTracker.screenDetailsOpened(userName = user.name)
    }
    private fun setupToolbar(userName: String) {
        supportActionBar?.hide()

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun bindUserData(user: User) {
        binding.tvAvatar.text   = user.name.first().uppercaseChar().toString()
        binding.tvName.text     = user.name
        binding.tvUsername.text = "@${user.username}"
        binding.tvEmail.text    = user.email
        binding.tvPhone.text    = user.phone
        binding.tvWebsite.text  = user.website
        binding.tvAddress.text  = user.address
    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }
}