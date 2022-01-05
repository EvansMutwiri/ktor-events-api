package com.eazybytes.plugins

import com.eazybytes.entities.UserEntity
import com.eazybytes.entities.db.DatabaseConnection
import com.eazybytes.models.EventResponse
import com.eazybytes.models.UserInfo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.ktorm.dsl.*

fun Application.configureAuthentication() {
    val db = DatabaseConnection.database

    routing {
        post("/register") {
            val userInfo = call.receive<UserInfo>()

            //validate user credentials before posting to db

            if (!userInfo.isValidCredentials()) {
                call.respond(HttpStatusCode.BadRequest, EventResponse(
                    data = "Invalid credentials. Username must be minimum 4 characters and password minimum 6 characters long.",
                    success = false
                ))
                return@post
            }
            val username = userInfo.username.lowercase()
            val password = userInfo.hashedPassword()

            //check if username already exists

            val user = db.from(UserEntity).select().where {
                UserEntity.username eq username
            }.map {
                it[UserEntity.username]
            }.firstOrNull()

            if (user != null) {
                call.respond(HttpStatusCode.BadRequest, EventResponse(
                    data = "Username already exists!",
                    success = false
                ))
                return@post
            } else {
                db.insert(UserEntity) {
                    set(it.username, username)
                    set(it.password, password)
                }
                call.respond(HttpStatusCode.Created, EventResponse(
                    data = "Username $username",
                    success = true
                ))
            }
        }
    }
}