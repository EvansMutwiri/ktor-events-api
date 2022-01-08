package com.eazybytes.plugins

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*

fun Application.jwtAuth() {
    install(Authentication) {
        jwt {

        }
    }
}