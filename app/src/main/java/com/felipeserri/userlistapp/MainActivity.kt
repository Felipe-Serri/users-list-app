package com.felipeserri.userlistapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.felipeserri.userlistapp.databinding.ActivityMainBinding
import com.felipeserri.userlistapp.model.UiState
import com.felipeserri.userlistapp.model.User
import com.felipeserri.userlistapp.ui.DetailActivity
import com.felipeserri.userlistapp.ui.UserAdapter
import com.felipeserri.userlistapp.utils.AnalyticsTracker
import com.felipeserri.userlistapp.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSwipeRefresh()
        setupRetryButton()
        observeViewModel()

        // Busca os dados passando o Context
        viewModel.fetchUsers(this)

        AnalyticsTracker.screenListOpened()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light
        )
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchUsers(this)
        }
    }

    private fun setupRetryButton() {
        binding.btnRetry.setOnClickListener {
            viewModel.fetchUsers(this)
        }
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is UiState.Loading -> showLoading()
                is UiState.Success -> showSuccess(state.users)
                is UiState.Error   -> showError(state.message)
            }
        }
    }
    private fun showLoading() {
        if (binding.swipeRefreshLayout.visibility == View.VISIBLE) {
            binding.swipeRefreshLayout.isRefreshing = true
            return
        }

        binding.layoutLoading.visibility = View.VISIBLE
        binding.layoutError.visibility = View.GONE
        binding.swipeRefreshLayout.visibility = View.GONE
    }

    private fun showSuccess(users: List<User>) {
        binding.swipeRefreshLayout.isRefreshing = false
        binding.layoutLoading.visibility = View.GONE
        binding.layoutError.visibility = View.GONE
        binding.recyclerView.adapter = UserAdapter(users) { user ->
            AnalyticsTracker.userClicked(userId = user.id, userName = user.name)

            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_ID,       user.id)
                putExtra(DetailActivity.EXTRA_NAME,     user.name)
                putExtra(DetailActivity.EXTRA_USERNAME, user.username)
                putExtra(DetailActivity.EXTRA_EMAIL,    user.email)
                putExtra(DetailActivity.EXTRA_PHONE,    user.phone)
                putExtra(DetailActivity.EXTRA_WEBSITE,  user.website)
                putExtra(DetailActivity.EXTRA_STREET,   user.address.street)
                putExtra(DetailActivity.EXTRA_CITY,     user.address.city)
            }
            startActivity(intent)
        }
        if (binding.swipeRefreshLayout.visibility != View.VISIBLE) {
            val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            binding.swipeRefreshLayout.startAnimation(fadeIn)
        }

        binding.swipeRefreshLayout.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        binding.swipeRefreshLayout.isRefreshing = false

        binding.layoutError.visibility = View.VISIBLE
        binding.layoutLoading.visibility = View.GONE
        binding.swipeRefreshLayout.visibility = View.GONE
        binding.tvError.text = message
    }
}