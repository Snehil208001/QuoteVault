package com.BrewApp.dailyquoteapp.data.auth


import android.util.Log
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email

class AuthManager {
    private val supabase = SupabaseClient.client

    // Check if user is logged in
    suspend fun isUserLoggedIn(): Boolean {
        return try {
            supabase.auth.currentUserOrNull() != null
        } catch (e: Exception) {
            Log.e("AuthManager", "Error checking login status", e)
            false
        }
    }

    // Get current user email
    suspend fun getCurrentUserEmail(): String? {
        return try {
            supabase.auth.currentUserOrNull()?.email
        } catch (e: Exception) {
            Log.e("AuthManager", "Error getting user email", e)
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
            Log.e("AuthManager", "Sign up failed", e)
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
            Log.e("AuthManager", "Sign in failed", e)
            // Check for specific Supabase error messages
            val errorMessage = e.message ?: ""
            if (errorMessage.contains("Email not confirmed", ignoreCase = true)) {
                AuthResult.Error("Please verify your email address to login.")
            } else if (errorMessage.contains("Invalid login credentials", ignoreCase = true)) {
                AuthResult.Error("Invalid email or password.")
            } else {
                AuthResult.Error(errorMessage.ifBlank { "Sign in failed" })
            }
        }
    }

    // Sign out
    suspend fun signOut(): AuthResult {
        return try {
            supabase.auth.signOut()
            AuthResult.Success
        } catch (e: Exception) {
            Log.e("AuthManager", "Sign out failed", e)
            AuthResult.Error(e.message ?: "Sign out failed")
        }
    }

    // Reset password
    suspend fun resetPassword(email: String): AuthResult {
        return try {
            supabase.auth.resetPasswordForEmail(email)
            AuthResult.Success
        } catch (e: Exception) {
            Log.e("AuthManager", "Password reset failed", e)
            AuthResult.Error(e.message ?: "Password reset failed")
        }
    }
}

sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}