package com.eazybytes.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.eazybytes.models.User.User
import io.ktor.config.*
import java.util.*

class TokenManager(config: HoconApplicationConfig) {
    val secret = config.property("secret").getString()
    val issuer = config.property("issuer").getString()
    val audience = config.property("audience").getString()
    val myRealm = config.property("realm").getString()
    fun generateToken(user: User): String {

//        val expirationDate = System.currentTimeMillis()+604800000;

        val token = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("username", user.username)
            .withClaim("userId", user.id)
            .withExpiresAt(Date(System.currentTimeMillis() + 604800000))
            .sign(Algorithm.HMAC256(secret))
//        call.respond(hashMapOf("token" to token))
        return token
    }

    fun verifyJWT(): JWTVerifier {
        return JWT.require(Algorithm.HMAC256(secret))
            .withAudience()
            .withIssuer()
            .build()
    }
}