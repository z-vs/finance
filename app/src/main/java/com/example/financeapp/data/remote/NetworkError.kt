package com.example.financeapp.data.remote

import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toUserMessage(): String = when (this) {
    is SocketTimeoutException -> "Сервер не отвечает. Проверьте подключение."
    is ConnectException       -> "Нет подключения к серверу. Запустите сервер."
    is UnknownHostException   -> "Нет интернета. Проверьте подключение."
    is HttpException           -> when (this.code()) {
        400 -> "Неверный запрос"
        401 -> "Необходима авторизация"
        403 -> "Доступ запрещён"
        404 -> "Данные не найдены"
        409 -> "Данные уже существуют"
        500 -> "Ошибка сервера"
        else -> "Ошибка сети: ${this.code()}"
    }
    else -> this.message ?: "Неизвестная ошибка"
}
