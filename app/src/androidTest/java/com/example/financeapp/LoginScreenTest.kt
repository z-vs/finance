package com.example.financeapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.financeapp.presentation.auth.AuthState
import com.example.financeapp.presentation.auth.LoginScreen
import com.example.financeapp.presentation.auth.AuthViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun заголовок_отображается() {
        composeTestRule.setContent {
            LoginScreen(
                onLoginSuccess = {},
                onRegisterClick = {}
            )
        }

        composeTestRule
            .onNodeWithText("Учёт финансов")
            .assertIsDisplayed()
    }

    @Test
    fun поля_email_и_пароль_отображаются() {
        composeTestRule.setContent {
            LoginScreen(
                onLoginSuccess = {},
                onRegisterClick = {}
            )
        }

        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Пароль").assertIsDisplayed()
    }

    @Test
    fun кнопка_войти_неактивна_когда_поля_пустые() {
        composeTestRule.setContent {
            LoginScreen(
                onLoginSuccess = {},
                onRegisterClick = {}
            )
        }

        composeTestRule
            .onNodeWithText("Войти")
            .assertIsNotEnabled()
    }

    @Test
    fun кнопка_войти_активна_после_заполнения_полей() {
        composeTestRule.setContent {
            LoginScreen(
                onLoginSuccess = {},
                onRegisterClick = {}
            )
        }

        composeTestRule
            .onNodeWithText("Email")
            .performTextInput("test@test.com")

        composeTestRule
            .onNodeWithText("Пароль")
            .performTextInput("123456")

        composeTestRule
            .onNodeWithText("Войти")
            .assertIsEnabled()
    }

    @Test
    fun кнопка_регистрации_отображается() {
        composeTestRule.setContent {
            LoginScreen(
                onLoginSuccess = {},
                onRegisterClick = {}
            )
        }

        composeTestRule
            .onNodeWithText("Нет аккаунта? Зарегистрироваться")
            .assertIsDisplayed()
    }
}