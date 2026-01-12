package com.BrewApp.dailyquoteapp.data.auth


import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email

class AuthManager {
    private val supabase = SupabaseClient.client

    // Check if user is logged in
    suspend fun isUserLoggedIn(): Boolean {
        return try {
            supabase.auth.currentUserOrNull() != null
        } catch (e: Exception) {
            false
        }
    }

    // Get current user email
    suspend fun getCurrentUserEmail(): String? {
        return try {
            supabase.auth.currentUserOrNull()?.email
        } catch (e: Exception) {
            null
        }
    }

    // Sign up with email and password
    suspend fun signUp(email: String, password: String): AuthResult {
        return try {
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Sign up failed")
        }
    }

    // Sign in with email and password
    suspend fun signIn(email: String, password: String): AuthResult {
        return try {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Sign in failed")
        }
    }

    // Sign out
    suspend fun signOut(): AuthResult {
        return try {
            supabase.auth.signOut()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Sign out failed")
        }
    }

    // Reset password
    suspend fun resetPassword(email: String): AuthResult {
        return try {
            supabase.auth.resetPasswordForEmail(email)
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Password reset failed")
        }
    }
}

sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}