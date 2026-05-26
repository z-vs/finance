package com.example.financeapp.presentation.categories

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.financeapp.domain.model.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    onBack: () -> Unit,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val state              by viewModel.state.collectAsState()
    var showAddDialog      by remember { mutableStateOf(false) }
    var editingCategory    by remember { mutableStateOf<Category?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title          = { Text("Категории") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        }
    ) { padding ->
        when (val s = state) {
            is CategoriesState.Loading -> {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is CategoriesState.Error -> {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text(s.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is CategoriesState.Success -> {
                if (s.categories.isEmpty()) {
                    Box(
                        modifier         = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text  = "Нет категорий — создайте первую",
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                } else {
                    LazyColumn(
                        modifier            = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(s.categories) { category ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier              = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment     = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text  = category.name,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text  = if (category.type == "income") "Доход" else "Расход",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (category.type == "income")
                                                Color(0xFF2E7D32) else Color(0xFFC62828)
                                        )
                                    }
                                    IconButton(onClick = { editingCategory = category }) {
                                        Icon(
                                            Icons.Default.Edit,
                                            contentDescription = "Редактировать",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    IconButton(onClick = { viewModel.deleteCategory(category.id) }) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Удалить",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddCategoryDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, type ->
                viewModel.createCategory(name, type)
                showAddDialog = false
            }
        )
    }

    editingCategory?.let { category ->
        EditCategoryDialog(
            category  = category,
            onDismiss = { editingCategory = null },
            onConfirm = { name, type ->
                viewModel.updateCategory(category.id, name, type)
                editingCategory = null
            }
        )
    }
}

@Composable
fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("expense") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title            = { Text("Новая категория") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value         = name,
                    onValueChange = { name = it },
                    label         = { Text("Название") },
                    modifier      = Modifier.fillMaxWidth()
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = type == "expense", onClick = { type = "expense" })
                    Text("Расход")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(selected = type == "income", onClick = { type = "income" })
                    Text("Доход")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick  = { onConfirm(name, type) },
                enabled  = name.isNotBlank()
            ) { Text("Создать") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}

@Composable
fun EditCategoryDialog(
    category: Category,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(category.name) }
    var type by remember { mutableStateOf(category.type) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title            = { Text("Редактировать категорию") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value         = name,
                    onValueChange = { name = it },
                    label         = { Text("Название") },
                    modifier      = Modifier.fillMaxWidth()
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = type == "expense", onClick = { type = "expense" })
                    Text("Расход")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(selected = type == "income", onClick = { type = "income" })
                    Text("Доход")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick  = { onConfirm(name, type) },
                enabled  = name.isNotBlank()
            ) { Text("Сохранить") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}