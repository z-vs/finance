package com.example.financeapp.presentation.dashboard

import android.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.financeapp.domain.model.Transaction
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

@Composable
fun ExpenseChart(transactions: List<Transaction>) {
    val expenses = transactions.filter { it.type == "expense" }
    if (expenses.isEmpty()) return

    val byCategory = expenses
        .groupBy { it.categoryName }
        .map { (name, txs) -> name to txs.sumOf { it.amount }.toFloat() }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text  = "Расходы по категориям",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                factory  = { context ->
                    PieChart(context).apply {
                        description.isEnabled  = false
                        isDrawHoleEnabled      = true
                        holeRadius             = 40f
                        setHoleColor(android.graphics.Color.TRANSPARENT)
                        legend.isEnabled       = true
                        setEntryLabelColor(android.graphics.Color.BLACK)
                        setEntryLabelTextSize(11f)
                    }
                },
                update = { chart ->
                    val entries = byCategory.map { (name, value) ->
                        PieEntry(value, name)
                    }
                    val colors = listOf(
                        Color.rgb(239, 83, 80),
                        Color.rgb(255, 167, 38),
                        Color.rgb(102, 187, 106),
                        Color.rgb(66, 165, 245),
                        Color.rgb(171, 71, 188),
                        Color.rgb(38, 198, 218),
                        Color.rgb(255, 112, 67)
                    )
                    val dataSet = PieDataSet(entries, "").apply {
                        this.colors = colors
                        valueTextSize  = 12f
                        valueTextColor = Color.BLACK
                    }
                    chart.data = PieData(dataSet)
                    chart.invalidate()
                }
            )
        }
    }
}