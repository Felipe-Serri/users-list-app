package com.felipeserri.userlistapp.utils

import android.util.Log

object AnalyticsTracker {
    private const val TAG = "AnalyticsTracker"
    private fun track(eventName: String, params: Map<String, Any> = emptyMap()) {
        val paramsFormatted = if (params.isEmpty()) {
            "sem parâmetros"
        } else {
            params.entries.joinToString(", ") { "${it.key}=${it.value}" }
        }
        Log.d(TAG, "EVENT: $eventName | PARAMS: $paramsFormatted")
    }

    fun screenListOpened() {
        track(
            eventName = "screen_list_opened"
        )
    }
    fun userClicked(userId: Int, userName: String) {
        track(
            eventName = "user_clicked",
            params = mapOf(
                "user_id"   to userId,
                "user_name" to userName
            )
        )
    }

    fun screenDetailsOpened(userName: String) {
        track(
            eventName = "screen_details_opened",
            params = mapOf(
                "user_name" to userName
            )
        )
    }
}