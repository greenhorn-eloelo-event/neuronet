package com.greenhorn.neuronet.model

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: Long = 0L, // Unique ID for the event
    val eventName: String,
    val isUserLogin: Boolean,
    val payload: Map<String, Any>,
    val timestamp: String,
    val sessionTimeStamp: String,
    val isSynced: Boolean = false, // To track if the event has been sent
    val primaryId: String,
    val sessionId: String,
)