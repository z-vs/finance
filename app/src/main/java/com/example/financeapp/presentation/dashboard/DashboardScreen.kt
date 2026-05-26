package com.example.financeapp.presentation.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.financeapp.domain.model.Transaction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onTransactionsClick: () -> Unit,
    onCategoriesClick: () -> Unit,
    onLogout: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadDashboard()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Главная") },
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Выйти")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick  = {},
                    icon     = { Icon(Icons.Default.List, contentDescription = null) },
                    label    = { Text("Главная") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick  = onTransactionsClick,
                    icon     = { Icon(Icons.Default.List, contentDescription = null) },
                    label    = { Text("Транзакции") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick  = onCategoriesClick,
                    icon     = { Icon(Icons.Default.Category, contentDescription = null) },
                    label    = { Text("Категории") }
                )
            }
        }
    ) { padding ->
        when (val s = state) {
            is DashboardState.Loading -> {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is DashboardState.Error -> {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text(s.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is DashboardState.Success -> {
                LazyColumn(
                    modifier            = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors   = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Баланс", style = MaterialTheme.typography.labelLarge)
                                Text(
                                    text  = "%.2f ₽".format(s.data.totalBalance),
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            }
                        }
                    }

                    item {
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Card(
                                modifier = Modifier.weight(1f),
                                colors   = CardDefaults.cardColors(
                                    containerColor = Color(0xFFE8F5E9)
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Доходы", style = MaterialTheme.typography.labelMedium)
                                    Text(
                                        text  = "+%.2f ₽".format(s.data.totalIncome),
                                        color = Color(0xFF2E7D32),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                            Card(
                                modifier = Modifier.weight(1f),
                                colors   = CardDefaults.cardColors(
                                    containerColor = Color(0xFFFFEBEE)
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Расходы", style = MaterialTheme.typography.labelMedium)
                                    Text(
                                        text  = "-%.2f ₽".format(s.data.totalExpense),
                                        color = Color(0xFFC62828),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    }

                    item {
                        ExpenseChart(transactions = s.data.allTransactions)
                    }

                    item {
                        Text(
                            text  = "Последние транзакции",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    if (s.data.recentTransactions.isEmpty()) {
                        item {
                            Box(
                                modifier         = Modifier.fillMaxWidth().padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text  = "Нет транзакций — добавьте первую",
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    } else {
                        items(s.data.recentTransactions) { transaction ->
                            TransactionItem(transaction)
                        }
                    }
                }
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title            = { Text("Выход") },
            text             = { Text("Вы уверены что хотите выйти?") },
            confirmButton    = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    viewModel.logout()
                    onLogout()
                }) { Text("Выйти", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton    = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Отмена") }
            }
        )
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text  = transaction.categoryName,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text  = if (transaction.type == "income") "Доход" else "Расход",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Text(
                text  = if (transaction.type == "income")
                    "+%.2f ₽".format(transaction.amount)
                else
                    "-%.2f ₽".format(transaction.amount),
                color = if (transaction.type == "income") Color(0xFF2E7D32) else Color(0xFFC62828),
                style = MaterialTheme.typography.titleMedium
            )
        }

    }
}