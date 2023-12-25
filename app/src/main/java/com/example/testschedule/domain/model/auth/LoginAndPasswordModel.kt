package com.example.testschedule.domain.model.auth

/**
 * ### LoginAndPasswordModel - Модель с 2 полями для авторизации в iis
 * * __username__ - имя пользователя (студак из 8 цифр)
 * * __password__ - пароль от аккаунта
 */
data class LoginAndPasswordModel(
    val username: String, // 253500xx
    val password: String // qwerty_123
)