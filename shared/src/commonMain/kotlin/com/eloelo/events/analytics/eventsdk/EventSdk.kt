package com.eloelo.events.analytics.eventsdk

interface EventSdk {
    fun trackEvent(eventName: String, isUserLogin : Boolean, payload: MutableMap<String, String>)
    fun forceUploadEvents()
    fun getPendingEventCount(): Long
}