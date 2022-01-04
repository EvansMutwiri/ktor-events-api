package com.eazybytes.plugins

import com.eazybytes.entities.EventEntity
import com.eazybytes.entities.db.DatabaseConnection
import com.eazybytes.models.EventItem
import com.eazybytes.models.EventResponse
import com.eazybytes.models.EventsRequest
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.ktorm.dsl.*

fun Application.configureRouting() {

    val db = DatabaseConnection.database

    routing {

        //get all events
        get("/events") {
//            call.respondText("return all events")

            /**
             * @Serializable
             * add dependency
             */

            val events = db.from(EventEntity).select().map {
                val id = it[EventEntity.id]
                val name = it[EventEntity.name]
                val description = it[EventEntity.description]
                val poster = it[EventEntity.poster]
                val price = it[EventEntity.price]
                EventItem(
                    id ?: -1,
                    description ?: "",
                    name ?: "",
                    poster ?: "",
                    price ?: "")
            }
            call.respond(events)
        }

        //get event by id
        get("/events/{id}") {
            val ide = call.parameters["id"]?.toInt() ?: -1
            val event = db.from(EventEntity).select().where { EventEntity.id eq ide }.map {
                val id = it[EventEntity.id]!!
                val name = it[EventEntity.name]!!
                val description = it[EventEntity.description]!!
                val poster = it[EventEntity.poster]!!
                val price = it[EventEntity.price]!!

                EventItem(
                    id = id,
                    description = description,
                    name = name,
                    poster = poster,
                    price = price)
            }.firstOrNull()

            if (event == null) {
                call.respond(HttpStatusCode.BadRequest, EventResponse(
                    success = false,
                    data = "Could not find event with id $ide"
                ))
            } else {
                call.respond(HttpStatusCode.OK, EventResponse(
                    success = true,
                    data = event
                ))
            }
        }

        //create new event
        post("/events") {
            val request = call.receive<EventsRequest>()
            val result = db.insert(EventEntity) {
                set(it.name, request.name)
                set(it.description, request.description)
                set(it.price, request.price)
                set(it.poster, request.poster)
            }
            if (result == 1) {
                //success response
                call.respond(HttpStatusCode.OK, EventResponse(
                    success = true,
                    data = "Successfully inserted"
                ))

            } else {
                //send failure message
                call.respond(HttpStatusCode.BadRequest, EventResponse(
                    success = false,
                    data = "Something went wrong. Failed"
                ))
            }
        }

        //edit event

        put("/events/{id}") {
            val ide = call.parameters["id"]?.toInt() ?: -1
            val updatedEvent = call.receive<EventsRequest>()

            val rowsAffected = db.update(EventEntity) {
                set(it.poster, updatedEvent.poster)
                set(it.name, updatedEvent.name)
                set(it.description, updatedEvent.description)
                set(it.price, updatedEvent.price)

                where {
                    it.id eq ide
                }
            }
            if (rowsAffected == 1)
                call.respond(
                    HttpStatusCode.OK,
                    EventResponse(success = true, data = "Event successfully updated")
                )
            else {
                call.respond(
                    HttpStatusCode.BadRequest,
                    EventResponse(success = false, data = "Event update failed")
                )
            }
        }

        //delete event

        delete("/events/{id}") {
            val ide = call.parameters["id"]?.toInt() ?: -1
            val rowsAffected = db.delete(EventEntity) {
                it.id eq ide
            }
            if(rowsAffected == 1) {
                call.respond(HttpStatusCode.OK, EventResponse(
                    data = "Event deleted successfully",
                    success = true
                ))
            } else {
                call.respond(HttpStatusCode.BadRequest, EventResponse(
                    data = "Event delete Failed",
                    success = false
                ))
            }
        }
    }
}

