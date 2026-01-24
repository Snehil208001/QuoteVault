package com.BrewApp.dailyquoteapp.mainui.loginscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.BrewApp.dailyquoteapp.data.auth.AuthManager
import com.BrewApp.dailyquoteapp.data.auth.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val authManager = AuthManager()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    // Track last reset request time to prevent rate limiting
    private var lastResetRequestTime = 0L
    private val RESET_COOLDOWN_MS = 25000L // 25 seconds

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    fun login() {
        if (_email.value.isBlank() || _password.value.isBlank()) {
            _loginState.value = LoginState.Error("Please fill in all fields")
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            when (val result = authManager.signIn(_email.value, _password.value)) {
                is AuthResult.Success -> {
                    _loginState.value = LoginState.Success
                }
                is AuthResult.Error -> {
                    _loginState.value = LoginState.Error(result.message)
                }
            }
        }
    }

    // Old method - kept for backwards compatibility
    fun resetPassword() {
        if (_email.value.isBlank()) {
            _loginState.value = LoginState.Error("Please enter your email")
            return
        }
        resetPasswordWithEmail(_email.value)
    }

    // New method - accepts email parameter with rate limiting
    fun resetPasswordWithEmail(email: String) {
        if (email.isBlank()) {
            _loginState.value = LoginState.Error("Please enter your email")
            return
        }

        // Basic email validation
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _loginState.value = LoginState.Error("Please enter a valid email address")
            return
        }

        // Prevent rapid successive calls (rate limit protection)
        val currentTime = System.currentTimeMillis()
        val timeSinceLastRequest = currentTime - lastResetRequestTime
        if (timeSinceLastRequest < RESET_COOLDOWN_MS) {
            val remainingSeconds = ((RESET_COOLDOWN_MS - timeSinceLastRequest) / 1000).toInt()
            _loginState.value = LoginState.Error("Please wait $remainingSeconds seconds before requesting another reset")
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            lastResetRequestTime = System.currentTimeMillis()

            when (val result = authManager.resetPassword(email)) {
                is AuthResult.Success -> {
                    _loginState.value = LoginState.PasswordResetSent
                }
                is AuthResult.Error -> {
                    _loginState.value = LoginState.Error(result.message)
                    // Reset the timer if there was an error (allow retry sooner)
                    if (!result.message.contains("wait", ignoreCase = true)) {
                        lastResetRequestTime = 0L
                    }
                }
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    object PasswordResetSent : LoginState()
    data class Error(val message: String) : LoginState()
}