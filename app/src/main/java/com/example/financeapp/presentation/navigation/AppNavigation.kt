package com.example.financeapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.financeapp.presentation.auth.LoginScreen
import com.example.financeapp.presentation.auth.RegisterScreen
import com.example.financeapp.presentation.dashboard.DashboardScreen
import com.example.financeapp.presentation.transactions.TransactionsScreen
import com.example.financeapp.presentation.transactions.TransactionDetailScreen
import com.example.financeapp.presentation.categories.CategoriesScreen
import com.example.financeapp.domain.model.Transaction

sealed class Screen(val route: String) {
    object Login              : Screen("login")
    object Register           : Screen("register")
    object Dashboard          : Screen("dashboard")
    object Transactions       : Screen("transactions")
    object TransactionDetail  : Screen("transaction_detail")
    object Categories         : Screen("categories")
}

object TransactionHolder {
    var selected: Transaction? = null
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController    = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess  = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onLoginClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onTransactionsClick = { navController.navigate(Screen.Transactions.route) },
                onCategoriesClick   = { navController.navigate(Screen.Categories.route) },
                onLogout            = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Transactions.route) {
            TransactionsScreen(
                onBack = { navController.popBackStack() },
                onTransactionClick = { transaction ->
                    TransactionHolder.selected = transaction
                    navController.navigate(Screen.TransactionDetail.route)
                }
            )
        }

        composable(Screen.TransactionDetail.route) {
            val transaction = TransactionHolder.selected
            if (transaction != null) {
                TransactionDetailScreen(
                    transaction = transaction,
                    onBack      = { navController.popBackStack() }
                )
            }
        }

        composable(Screen.Categories.route) {
            CategoriesScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}