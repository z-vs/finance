package com.example.financeapp.presentation.transactions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.financeapp.domain.model.Category
import com.example.financeapp.domain.model.Transaction
import com.example.financeapp.presentation.categories.CategoriesState
import com.example.financeapp.presentation.categories.CategoriesViewModel
import com.example.financeapp.presentation.components.ErrorSnackbarHost

@Composable
fun TransactionsScreen(
    onBack: () -> Unit,
    onTransactionClick: (Transaction) -> Unit = {},
    viewModel: TransactionsViewModel = hiltViewModel(),
    categoriesViewModel: CategoriesViewModel = hiltViewModel()
) {
    val state           by viewModel.state.collectAsState()
    val categoriesState by categoriesViewModel.state.collectAsState()

    TransactionsScreenContent(
        state              = state,
        categoriesState    = categoriesState,
        onBack             = onBack,
        onTransactionClick = onTransactionClick,
        onDelete           = { viewModel.deleteTransaction(it) },
        onCreate           = { categoryId, amount, type, description ->
            viewModel.createTransaction(
                categoryId  = categoryId,
                amount      = amount,
                type        = type,
                description = description,
                date        = System.currentTimeMillis()
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreenContent(
    state: TransactionsState,
    categoriesState: CategoriesState = CategoriesState.Loading,
    onBack: () -> Unit,
    onTransactionClick: (Transaction) -> Unit = {},
    onDelete: (Int) -> Unit,
    onCreate: (Int, Double, String, String?) -> Unit
) {
    var showAddDialog    by remember { mutableStateOf(false) }
    var filter           by remember { mutableStateOf("all") }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state) {
        if (state is TransactionsState.Error) {
            snackbarHostState.showSnackbar(state.message)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title          = { Text("Транзакции") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        },
        snackbarHost         = { ErrorSnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Row(
                modifier              = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(selected = filter == "all",     onClick = { filter = "all" },     label = { Text("Все") })
                FilterChip(selected = filter == "income",  onClick = { filter = "income" },  label = { Text("Доходы") })
                FilterChip(selected = filter == "expense", onClick = { filter = "expense" }, label = { Text("Расходы") })
            }

            when (val s = state) {
                is TransactionsState.Loading -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
                }
                is TransactionsState.Error -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Text(s.message, color = MaterialTheme.colorScheme.error)
                    }
                }
                is TransactionsState.Success -> {
                    val filtered = when (filter) {
                        "income"  -> s.transactions.filter { it.type == "income" }
                        "expense" -> s.transactions.filter { it.type == "expense" }
                        else      -> s.transactions
                    }
                    if (filtered.isEmpty()) {
                        Box(Modifier.fillMaxSize(), Alignment.Center) {
                            Text("Нет транзакций", color = MaterialTheme.colorScheme.outline)
                        }
                    } else {
                        LazyColumn(
                            modifier            = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding      = PaddingValues(vertical = 8.dp)
                        ) {
                            items(filtered) { transaction ->
                                TransactionCard(
                                    transaction = transaction,
                                    onClick     = { onTransactionClick(transaction) },
                                    onDelete    = { onDelete(transaction.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddTransactionDialog(
            categoriesState = categoriesState,
            onDismiss       = { showAddDialog = false },
            onConfirm       = { categoryId, amount, type, description ->
                onCreate(categoryId, amount, type, description)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun TransactionCard(
    transaction: Transaction,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier              = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(transaction.categoryName, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text  = if (transaction.type == "income") "Доход" else "Расход",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (transaction.type == "income") Color(0xFF2E7D32) else Color(0xFFC62828)
                )
            }
            Text(
                text  = if (transaction.type == "income") "+%.2f ₽".format(transaction.amount)
                else "-%.2f ₽".format(transaction.amount),
                color = if (transaction.type == "income") Color(0xFF2E7D32) else Color(0xFFC62828),
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Удалить",
                    tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int, Double, String, String?) -> Unit,
    categoriesState: CategoriesState = CategoriesState.Loading
) {
    var amount           by remember { mutableStateOf("") }
    var description      by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var expanded         by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Новая транзакция") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value         = amount,
                    onValueChange = { amount = it },
                    label         = { Text("Сумма") },
                    modifier      = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value         = description,
                    onValueChange = { description = it },
                    label         = { Text("Описание (необязательно)") },
                    modifier      = Modifier.fillMaxWidth()
                )
                ExposedDropdownMenuBox(
                    expanded         = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value         = selectedCategory?.let {
                            "${it.name} (${if (it.type == "income") "Доход" else "Расход"})"
                        } ?: "",
                        onValueChange = {},
                        readOnly      = true,
                        label         = { Text("Категория") },
                        trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier      = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded         = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        when (val s = categoriesState) {
                            is CategoriesState.Success -> {
                                if (s.categories.isEmpty()) {
                                    DropdownMenuItem(
                                        text    = { Text("Нет категорий — создайте сначала") },
                                        onClick = { expanded = false }
                                    )
                                } else {
                                    s.categories.forEach { category ->
                                        DropdownMenuItem(
                                            text    = { Text("${category.name} (${if (category.type == "income") "Доход" else "Расход"})") },
                                            onClick = { selectedCategory = category; expanded = false }
                                        )
                                    }
                                }
                            }
                            else -> DropdownMenuItem(text = { Text("Загрузка...") }, onClick = {})
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val cat = selectedCategory ?: return@TextButton
                    onConfirm(cat.id, amount.toDoubleOrNull() ?: 0.0, cat.type, description.ifBlank { null })
                },
                enabled = selectedCategory != null && amount.isNotBlank()
            ) { Text("Добавить") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}