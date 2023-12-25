package com.example.testschedule.domain.model.auth

/**
 * ### UserBasicDataModel - иноформация о пользователе, которая возвращается после авторизации
 * *  [canStudentNote] (_false_) - может ли отмечать пропуски
 * *  [email] (_me@g.ru_) - почта студента
 * *  [fio] (_Иванов Иван Иванович_) - ФИО студента
 * *  [group] (_25xxxx_) - группа студента
 * *  [hasNotConfirmedContact] (_false_) - надо ли подтвержать почту
 * *  [isGroupHead] (_false_) - является ли старостой
 * *  [phone] (_+375xxxxxx_) - номер студента
 * *  [photoUrl] (_https://drive.google.com/uc?id=1w0kKMQi1eQPreMQBI2gSxm0AOc..._) - ссылка на google disk, если есть, иначе null
 * *  [username] (_25xxxxxx_) - студенческий
 * *  [cookie] (_xxxxxxxxx_) - куки, для авторизации в системе
 */
data class UserBasicDataModel(
    val canStudentNote: Boolean, // false
    val email: String?, // sashaxxxxxx@xxxxx.com
    val fio: String, // Иванов Иван Иванович
    val group: String, // 25xxxx
    val hasNotConfirmedContact: Boolean, // false
    val isGroupHead: Boolean, // false
    val phone: String, // +37529xxxxxxx
    val photoUrl: String?, // https://drive.google.com/uc?id=1w0kKMQi1eQPreMQBI2gSxm0AOc...
    val username: String, // 253500xx
    val cookie: String,
)