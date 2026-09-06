package com.example.testschedule.common

object CacheUpdateKeys {
    const val PROFILE = "account:profile"
    const val GROUP = "account:group"
    const val MARK_BOOK = "account:mark_book"
    const val RATING = "account:rating"
    const val OMISSIONS = "account:omissions"
    const val DORMITORY = "account:dormitory"
    const val PENALTY = "account:penalty"
    const val NOTIFICATIONS = "account:notifications"
    const val STUDY = "account:study"
    const val SETTINGS = "account:settings"
    const val HEADMAN = "account:headman"

    fun schedule(id: String) = "schedule:$id"
}
