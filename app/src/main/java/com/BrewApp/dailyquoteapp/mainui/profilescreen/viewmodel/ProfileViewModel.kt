package com.BrewApp.dailyquoteapp.mainui.profilescreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.BrewApp.dailyquoteapp.data.auth.AuthManager
import com.BrewApp.dailyquoteapp.data.auth.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val authManager = AuthManager()

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail: StateFlow<String?> = _userEmail.asStateFlow()

    fun loadUserData() {
        viewModelScope.launch {
            _userEmail.value = authManager.getCurrentUserEmail()
        }
    }

    fun logout() {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            when (val result = authManager.signOut()) {
                is AuthResult.Success -> {
                    _profileState.value = ProfileState.LogoutSuccess
                }
                is AuthResult.Error -> {
                    _profileState.value = ProfileState.Error(result.message)
                }
            }
        }
    }

    fun resetState() {
        _profileState.value = ProfileState.Idle
    }
}

sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    object LogoutSuccess : ProfileState()
    data class Error(val message: String) : ProfileState()
}