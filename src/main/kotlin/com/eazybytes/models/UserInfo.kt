package com.eazybytes.models

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val username: String,
    val password: String
    )
