package com.example.financeapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.financeapp.presentation.transactions.TransactionsScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TransactionsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun заголовок_транзакции_отображается() {
        composeTestRule.setContent {
            TransactionsScreen(onBack = {})
        }

        composeTestRule
            .onNodeWithText("Транзакции")
            .assertIsDisplayed()
    }

    @Test
    fun фильтры_все_доходы_расходы_отображаются() {
        composeTestRule.setContent {
            TransactionsScreen(onBack = {})
        }

        composeTestRule.onNodeWithText("Все").assertIsDisplayed()
        composeTestRule.onNodeWithText("Доходы").assertIsDisplayed()
        composeTestRule.onNodeWithText("Расходы").assertIsDisplayed()
    }

    @Test
    fun кнопка_добавить_отображается() {
        composeTestRule.setContent {
            TransactionsScreen(onBack = {})
        }

        composeTestRule
            .onNodeWithContentDescription("Добавить")
            .assertIsDisplayed()
    }

    @Test
    fun нажатие_назад_вызывает_onBack() {
        var backCalled = false

        composeTestRule.setContent {
            TransactionsScreen(onBack = { backCalled = true })
        }

        composeTestRule
            .onNodeWithContentDescription("Назад")
            .performClick()

        assert(backCalled)
    }
}