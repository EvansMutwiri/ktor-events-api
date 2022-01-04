package com.eazybytes.models

import kotlinx.serialization.Serializable


@Serializable
data class EventsRequest(
    val description: String,
    val name: String,
    val poster: String,
    val price: String

)
