package com.eazybytes.plugins

import com.eazybytes.models.UserInfo
import com.eazybytes.models.UserResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.io.File

fun Application.configureRouting() {

    routing {

        get("/headers") {
            call.response.headers.append("server", "ktor_server", safeOnly = true)
            call.respond("Headers attached")
        }
        get("/") {
            val userResponse = UserResponse(username = "evans")
            call.respond(userResponse)
        }

        post("/login") {
            val userInfo = call.receive<UserInfo>()
            println(userInfo)
            call.respond(userInfo.username)
        }
        get("/events") {
//            val eventItem = EventItem
            call.respond(mapOf("poster" to "this is a poster", "name" to "mr & ms isiolo"))
        }

        get("/download") {
            val file = File("src/imagess/shoppe.png")
            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Attachment.withParameter(
                    ContentDisposition.Parameters.FileName, "TermsConditions.png"
                ).toString()
            )
            call.respondText("File downloading...")
            call.respondFile(file)
        }

        get("/openImage") {
            val file = File("src/imagess/shoppe.png")
            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Inline.withParameter(
                    ContentDisposition.Parameters.FileName, "TermsConditions.png"
                ).toString()
            )
            call.respondFile(file)
        }
    }
}

