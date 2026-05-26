package com.example.financeapp.presentation.transactions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.financeapp.domain.model.Transaction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailScreen(
    transaction: Transaction,
    onBack: () -> Unit,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    var amount      by remember { mutableStateOf(transaction.amount.toString()) }
    var description by remember { mutableStateOf(transaction.description ?: "") }
    var type        by remember { mutableStateOf(transaction.type) }
    var categoryId  by remember { mutableStateOf(transaction.categoryId.toString()) }
    var isSaved     by remember { mutableStateOf(false) }

    LaunchedEffect(isSaved) {
        if (isSaved) onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title          = { Text("Детали транзакции") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.deleteTransaction(transaction.id)
                        onBack()
                    }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Удалить",
                            tint               = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value           = amount,
                onValueChange   = { amount = it },
                label           = { Text("Сумма") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier        = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value         = description,
                onValueChange = { description = it },
                label         = { Text("Описание") },
                modifier      = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value         = categoryId,
                onValueChange = { categoryId = it },
                label         = { Text("ID категории") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier      = Modifier.fillMaxWidth()
            )

            Text(
                text  = "Тип транзакции",
                style = MaterialTheme.typography.labelMedium
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = type == "expense",
                    onClick  = { type = "expense" }
                )
                Text("Расход")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = type == "income",
                    onClick  = { type = "income" }
                )
                Text("Доход")
            }

            Spacer(modifier = Modifier.weight(1f))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors   = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text  = "ID транзакции: ${transaction.id}",
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text  = "Дата: ${java.text.SimpleDateFormat(
                            "dd.MM.yyyy HH:mm",
                            java.util.Locale.getDefault()
                        ).format(java.util.Date(transaction.date))}",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Button(
                onClick = {
                    viewModel.updateTransaction(
                        id          = transaction.id,
                        categoryId  = categoryId.toIntOrNull() ?: transaction.categoryId,
                        amount      = amount.toDoubleOrNull() ?: transaction.amount,
                        description = description.ifBlank { null },
                        date        = transaction.date
                    )
                    isSaved = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сохранить изменения")
            }
        }
    }
}