package com.eazybytes.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.eazybytes.models.User.User
import io.ktor.config.*
import java.util.*

class TokenManager(private val config: HoconApplicationConfig) {
    fun generateToken(user: User): String {
        val secret = config.property("secret").getString()
        val issuer = config.property("issuer").getString()
        val audience = config.property("audience").getString()
        val myRealm = config.property("realm").getString()
//        val expirationDate = System.currentTimeMillis()+604800000;

        val token = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("username", user.username)
            .withExpiresAt(Date(System.currentTimeMillis() + 604800000))
            .sign(Algorithm.HMAC256(secret))
//        call.respond(hashMapOf("token" to token))
        return token
    }
}