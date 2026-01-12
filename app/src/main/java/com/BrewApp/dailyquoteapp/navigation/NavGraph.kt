package com.BrewApp.dailyquoteapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.BrewApp.dailyquoteapp.mainui.components.AppBottomNavBar
import com.BrewApp.dailyquoteapp.mainui.discoveryscreen.ui.DiscoveryScreen
import com.BrewApp.dailyquoteapp.mainui.favouritescreen.ui.FavouriteScreen
import com.BrewApp.dailyquoteapp.mainui.homescreen.ui.HomeScreen
import com.BrewApp.dailyquoteapp.mainui.loginscreen.ui.LoginScreen
import com.BrewApp.dailyquoteapp.mainui.profilescreen.ui.ProfileScreen
import com.BrewApp.dailyquoteapp.mainui.signupscreen.ui.SignUpScreen

@Composable
fun AppNavGraph(startDestination: String = Screens.Login.route) {
    val navController = rememberNavController()

    // Determine when to show the Bottom Bar
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in listOf(
        Screens.Home.route,
        Screens.Discovery.route,
        Screens.Favorites.route,
        Screens.Profile.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                AppBottomNavBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            // --- Authentication Screens ---

            composable(Screens.Login.route) {
                LoginScreen(
                    onLoginClick = {
                        // Clear back stack so user can't press back to return to login
                        navController.navigate(Screens.Home.route) {
                            popUpTo(Screens.Login.route) { inclusive = true }
                        }
                    },
                    onSignUpClick = {
                        navController.navigate(Screens.SignUp.route)
                    },
                    onForgotPasswordClick = {
                        // TODO: Navigate to Forgot Password screen
                    }
                )
            }

            composable(Screens.SignUp.route) {
                SignUpScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onLoginClick = {
                        // Go back to existing Login screen rather than creating a new one
                        navController.popBackStack()
                    },
                    onSignUpSubmit = {
                        // After signup, navigate to Home and clear auth screens
                        navController.navigate(Screens.Home.route) {
                            popUpTo(Screens.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            // --- Main App Screens ---

            composable(Screens.Home.route) {
                HomeScreen()
            }

            composable(Screens.Discovery.route) {
                DiscoveryScreen()
            }

            composable(Screens.Favorites.route) {
                FavouriteScreen(
                    onBackClick = { navController.popBackStack() },
                    onItemClick = { item ->
                        // TODO: Handle details navigation
                    }
                )
            }

            composable(Screens.Profile.route) {
                ProfileScreen(
                    onBackClick = { navController.popBackStack() },
                    onEditProfileClick = { /* TODO */ },
                    onSettingsClick = { /* TODO */ },
                    onNotificationsClick = { /* TODO */ },
                    onPreferencesClick = { /* TODO */ },
                    onLogoutClick = {
                        // Log out and return to Login, clearing the stack
                        navController.navigate(Screens.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}