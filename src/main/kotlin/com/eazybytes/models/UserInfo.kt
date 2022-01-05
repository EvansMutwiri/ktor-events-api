package com.eazybytes.models

import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
data class UserInfo(
    val username: String,
    val password: String
    ) {
    fun hashedPassword(): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }
    fun isValidCredentials(): Boolean {
        return username.length >= 4 && password.length >= 6 && !username.contains(" ") && !password.equals("password", ignoreCase = true)
    }
}
