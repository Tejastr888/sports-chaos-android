package com.shk.sportschaos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shk.sportschaos.data.local.TokenManager
import com.shk.sportschaos.data.repository.AuthRepository
import com.shk.sportschaos.ui.auth.LoginScreen
import com.shk.sportschaos.ui.auth.LoginViewModel
import com.shk.sportschaos.ui.auth.RegisterScreen      // ← ADD THIS
import com.shk.sportschaos.ui.auth.RegisterViewModel  // ← ADD THIS
import com.shk.sportschaos.ui.home.HomeScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
}

@Composable
fun NavGraph(
    navController: NavHostController,
    tokenManager: TokenManager
) {
    val authRepository = AuthRepository(tokenManager)

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        // ═══ LOGIN SCREEN ═══
        composable(Routes.LOGIN) {
            val viewModel = LoginViewModel(authRepository)

            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        // ═══════════════════════════════════════════════════
        // ═══ REGISTER SCREEN - ADD THIS BLOCK ═══
        // ═══════════════════════════════════════════════════
        composable(Routes.REGISTER) {
            val viewModel = RegisterViewModel(authRepository)

            RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = {
                    // After successful registration, go to home
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    // Go back to login screen
                    navController.popBackStack()
                }
            )
        }

        // ═══ HOME SCREEN ═══
        composable(Routes.HOME) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}