package com.eazybytes.plugins

import com.eazybytes.entities.EventEntity
import com.eazybytes.entities.db.DatabaseConnection
import com.eazybytes.models.EventItem
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import org.ktorm.dsl.from
import org.ktorm.dsl.map
import org.ktorm.dsl.select

fun Application.configureRouting() {

    val db = DatabaseConnection.database

    routing {
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
    }
}

