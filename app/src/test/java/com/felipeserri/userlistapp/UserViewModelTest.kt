package com.felipeserri.userlistapp

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.felipeserri.userlistapp.model.Address
import com.felipeserri.userlistapp.model.Company
import com.felipeserri.userlistapp.model.UiState
import com.felipeserri.userlistapp.model.User
import com.felipeserri.userlistapp.repository.UserRepository
import com.felipeserri.userlistapp.utils.NetworkUtils
import com.felipeserri.userlistapp.viewmodel.UserViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val repository = mockk<UserRepository>()

    // Mock do Context — simula o Context sem precisar de um dispositivo Android
    private val mockContext = mockk<Context>(relaxed = true)

    private lateinit var viewModel: UserViewModel

    private val fakeUsers = listOf(
        User(
            id       = 1,
            name     = "Felipe Serri",
            username = "felipeserri",
            email    = "felipe@teste.com",
            phone    = "11999999999",
            website  = "felipeserri.com",
            address  = Address(street = "Rua Teste", city = "São Paulo"),
            company  = Company(name = "Empresa Teste")
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkObject(NetworkUtils)
        every { NetworkUtils.isNetworkAvailable(any()) } returns true
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `quando repository retorna sucesso, estado deve ser Success com a lista`() = runTest {

        coEvery { repository.getUsers() } returns Result.success(fakeUsers)

        viewModel = UserViewModel(repository)
        viewModel.fetchUsers(mockContext)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("Estado deve ser Success", state is UiState.Success)

        val successState = state as UiState.Success
        assertEquals("Lista deve ter 1 usuário", 1, successState.users.size)
        assertEquals("Nome deve ser Felipe Serri", "Felipe Serri", successState.users[0].name)
    }

    @Test
    fun `quando repository retorna erro, estado deve ser Error com a mensagem`() = runTest {

        val errorMessage = "Sem conexão com a internet"
        coEvery { repository.getUsers() } returns Result.failure(Exception(errorMessage))

        viewModel = UserViewModel(repository)
        viewModel.fetchUsers(mockContext)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("Estado deve ser Error", state is UiState.Error)

        val errorState = state as UiState.Error
        assertEquals("Mensagem deve ser correta", errorMessage, errorState.message)
    }

    @Test
    fun `quando repository retorna sucesso, lista nao deve estar vazia`() = runTest {

        coEvery { repository.getUsers() } returns Result.success(fakeUsers)

        viewModel = UserViewModel(repository)
        viewModel.fetchUsers(mockContext)
        advanceUntilIdle()

        val state = viewModel.uiState.value as UiState.Success
        assertTrue("Lista não deve estar vazia", state.users.isNotEmpty())
        assertEquals("Lista deve ter exatamente 1 usuário", 1, state.users.size)
    }
}