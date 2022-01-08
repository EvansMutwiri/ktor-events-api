package com.eazybytes.plugins

import com.eazybytes.entities.UserEntity
import com.eazybytes.entities.db.DatabaseConnection
import com.eazybytes.models.EventResponse
import com.eazybytes.models.User.User
import com.eazybytes.models.UserInfo
import com.eazybytes.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.ktorm.dsl.*
import org.mindrot.jbcrypt.BCrypt

fun Application.configureAuthentication() {
    val db = DatabaseConnection.database
    val tokenManager = TokenManager(HoconApplicationConfig(ConfigFactory.load()))
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

        post("/login") {
            val userInfo = call.receive<UserInfo>()

            if (!userInfo.isValidCredentials()) {
                call.respond(
                    HttpStatusCode.BadRequest, EventResponse(
                        data = "Invalid credentials.",
                        success = false
                    )
                )
                return@post
            }
            val username = userInfo.username.lowercase()

            //use password from body
            val password = userInfo.password

            //check if user exists
            val user = db.from(UserEntity)
                .select()
                .where { UserEntity.username eq username }
                .map {
                    val id = it[UserEntity.id]!!
                    val lusername = it[UserEntity.id]!!
                    val lpassword = it[UserEntity.password]!!
                    User(id, lusername, lpassword)
                }.firstOrNull()

            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, EventResponse(
                    data = "Username does not exist",
                    success = false
                ))
                return@post
            }
            val passwordMatch = BCrypt.checkpw(password, user.password)
            if (!passwordMatch) {
                call.respond(HttpStatusCode.BadRequest, EventResponse(
                    data = "Username or password invalid",
                    success = false
                ))
                return@post
            }
            val token = tokenManager.generateToken(user)

            call.respond(HttpStatusCode.OK, EventResponse(
                data = token,
                success = true
            ))
        }
    }
}