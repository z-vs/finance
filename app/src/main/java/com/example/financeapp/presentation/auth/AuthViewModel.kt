package com.example.financeapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.domain.usecase.auth.LoginUseCase
import com.example.financeapp.domain.usecase.auth.RegisterUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state

    private val firebaseAuth = FirebaseAuth.getInstance()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            try {
                val result = firebaseAuth
                    .signInWithEmailAndPassword(email, password)
                    .await()
                val firebaseToken = result.user?.getIdToken(false)?.await()?.token
                    ?: throw Exception("Failed to get Firebase token")

                loginUseCase(firebaseToken)
                    .onSuccess { _state.value = AuthState.Success }
                    .onFailure { _state.value = AuthState.Error(it.message ?: "Login failed") }
            } catch (e: Exception) {
                _state.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            try {
                val result = firebaseAuth
                    .createUserWithEmailAndPassword(email, password)
                    .await()
                val firebaseToken = result.user?.getIdToken(false)?.await()?.token
                    ?: throw Exception("Failed to get Firebase token")

                registerUseCase(firebaseToken, email)
                    .onSuccess { _state.value = AuthState.Success }
                    .onFailure { _state.value = AuthState.Error(it.message ?: "Register failed") }
            } catch (e: Exception) {
                _state.value = AuthState.Error(e.message ?: "Register failed")
            }
        }
    }

    fun resetState() {
        _state.value = AuthState.Idle
    }
}