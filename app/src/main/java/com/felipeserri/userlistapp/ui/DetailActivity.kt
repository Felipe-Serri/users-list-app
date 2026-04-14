package com.felipeserri.userlistapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.felipeserri.userlistapp.databinding.ActivityDetailBinding
import com.felipeserri.userlistapp.utils.AnalyticsTracker

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name     = intent.getStringExtra(EXTRA_NAME)     ?: ""
        val username = intent.getStringExtra(EXTRA_USERNAME) ?: ""
        val email    = intent.getStringExtra(EXTRA_EMAIL)    ?: ""
        val phone    = intent.getStringExtra(EXTRA_PHONE)    ?: ""
        val website  = intent.getStringExtra(EXTRA_WEBSITE)  ?: ""
        val street   = intent.getStringExtra(EXTRA_STREET)   ?: ""
        val city     = intent.getStringExtra(EXTRA_CITY)     ?: ""

        setupToolbar(name)
        bindUserData(name, username, email, phone, website, street, city)
        AnalyticsTracker.screenDetailsOpened(userName = name)
    }
    private fun setupToolbar(userName: String) {
        supportActionBar?.apply {
            title = userName
            setDisplayHomeAsUpEnabled(true)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun bindUserData(
        name: String,
        username: String,
        email: String,
        phone: String,
        website: String,
        street: String,
        city: String
    ) {
        binding.tvAvatar.text   = name.first().uppercaseChar().toString()
        binding.tvName.text     = name
        binding.tvUsername.text = "@$username"
        binding.tvEmail.text    = email
        binding.tvPhone.text    = phone
        binding.tvWebsite.text  = website
        binding.tvAddress.text  = "$street, $city"
    }

    companion object {
        const val EXTRA_ID       = "extra_id"
        const val EXTRA_NAME     = "extra_name"
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_EMAIL    = "extra_email"
        const val EXTRA_PHONE    = "extra_phone"
        const val EXTRA_WEBSITE  = "extra_website"
        const val EXTRA_STREET   = "extra_street"
        const val EXTRA_CITY     = "extra_city"
    }
}