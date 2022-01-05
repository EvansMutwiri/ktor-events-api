package com.eazybytes.plugins

import com.eazybytes.entities.UserEntity
import com.eazybytes.entities.db.DatabaseConnection
import com.eazybytes.models.UserInfo
import com.eazybytes.models.UserResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.ktorm.dsl.insert

fun Application.configureAuthentication() {
    val db = DatabaseConnection.database

    routing {
        post("/register") {
            val userInfo = call.receive<UserInfo>()
            val username = userInfo.username.lowercase()
            val password = userInfo.hashedPassword()

            val rowsAffected = db.insert(UserEntity) {
                set(it.username, username)
                set(it.password, password)
            }
            if (rowsAffected == 1) {
                call.respond(HttpStatusCode.OK, UserResponse(
                    data = userInfo.username,
                    success = true
                ))
            } else {
                call.respond(HttpStatusCode.BadRequest, UserResponse(
                    data = "User registration failed",
                    success = false
                ))
            }
        }
    }
}