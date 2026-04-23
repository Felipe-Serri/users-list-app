// MainActivity.kt
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
import com.felipeserri.userlistapp.viewmodel.UserViewModelFactory
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // ✅ Usa a Factory para criar o ViewModel com o Repository correto
    private val viewModel: UserViewModel by viewModels {
        UserViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSwipeRefresh()
        setupRetryButton()
        observeViewModel()

        // ✅ fetchUsers sem Context
        viewModel.fetchUsers()
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
            viewModel.fetchUsers()
        }
    }

    private fun setupRetryButton() {
        binding.btnRetry.setOnClickListener {
            viewModel.fetchUsers()
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
            navigateToDetail(user)
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

    private fun navigateToDetail(user: User) {
        AnalyticsTracker.userClicked(userId = user.id, userName = user.name)
        val userJson = Gson().toJson(user)
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_USER, userJson)
        }
        startActivity(intent)
    }
}