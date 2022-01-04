package com.eazybytes.models

import kotlinx.serialization.Serializable

@Serializable
data class EventResponse<T>(
    val data: T,
    val success: Boolean
)
