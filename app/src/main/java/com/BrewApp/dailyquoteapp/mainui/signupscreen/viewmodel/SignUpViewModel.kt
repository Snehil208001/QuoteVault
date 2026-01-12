package com.BrewApp.dailyquoteapp.mainui.signupscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.BrewApp.dailyquoteapp.data.auth.AuthManager
import com.BrewApp.dailyquoteapp.data.auth.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {
    private val authManager = AuthManager()

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> = _signUpState.asStateFlow()

    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    fun updateFullName(name: String) {
        _fullName.value = name
    }

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    fun signUp() {
        if (_fullName.value.isBlank() || _email.value.isBlank() || _password.value.isBlank()) {
            _signUpState.value = SignUpState.Error("Please fill in all fields")
            return
        }

        if (_password.value.length < 6) {
            _signUpState.value = SignUpState.Error("Password must be at least 6 characters")
            return
        }

        viewModelScope.launch {
            _signUpState.value = SignUpState.Loading
            when (val result = authManager.signUp(_email.value, _password.value)) {
                is AuthResult.Success -> {
                    _signUpState.value = SignUpState.Success
                }
                is AuthResult.Error -> {
                    _signUpState.value = SignUpState.Error(result.message)
                }
            }
        }
    }

    fun resetState() {
        _signUpState.value = SignUpState.Idle
    }
}

sealed class SignUpState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState()
    data class Error(val message: String) : SignUpState()
}