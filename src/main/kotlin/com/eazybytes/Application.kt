package com.eazybytes

import com.eazybytes.plugins.configureAuthentication
import com.eazybytes.plugins.configureRouting
import com.eazybytes.plugins.configureSerialization
import com.eazybytes.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        val config = HoconApplicationConfig(ConfigFactory.load())
        val tokenManager = TokenManager(config)
        install(Authentication) {
            jwt {
                verifier(tokenManager.verifyJWT())
                realm = tokenManager.myRealm
                validate { jwtCredential ->
                    if (jwtCredential.payload.getClaim("username").asString().isNotEmpty()) {
                        JWTPrincipal(jwtCredential.payload)
                    } else {
                        null
                    }
                }
            }
        }
        configureAuthentication()
        configureSerialization()
        configureRouting()
    }.start(wait = true)
}
