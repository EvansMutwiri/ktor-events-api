package com.eazybytes

import com.eazybytes.entities.EventEntity
import com.eazybytes.entities.EventEntity.id
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.eazybytes.plugins.*
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSerialization()

        val database = Database.connect(
            url = "jdbc:mysql://localhost:3306/simpledb",
            driver = "com.mysql.cj.jdbc.Driver",
            user = "eazzy",
            password = "*******"
        )

        //delete
        database.delete(EventEntity) {
            it.id eq 1
        }

        //edit or update
        database.update(EventEntity) {
            set(it.name, "engineer")
            set(it.price, "3000")
            set(it.description, "trade fair")

            where {
                it.id eq 2
            }
        }

        //insert
        database.insert(EventEntity) {
            set(it.id, 1)
            set(it.name, "myevent")
            set(it.poster, "poster.link")
            set(it.description, "my event is a sample event")
            set(it.price, "2000")

        }

        //read from db
        var event = database.from(EventEntity).select()

        for (row in event) {
            println("${row[EventEntity.id]}: ${row[EventEntity.name]}, ${row[EventEntity.description]}")
        }
    }.start(wait = true)
}
