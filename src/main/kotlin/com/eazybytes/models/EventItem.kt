package com.eazybytes.models

import kotlinx.serialization.Serializable

@Serializable
data class EventItem(
    val id: Int,
    val description: String,
    val name: String,
    val poster: String,
    val price: String
)
