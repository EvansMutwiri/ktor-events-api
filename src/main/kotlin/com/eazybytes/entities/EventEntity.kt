package com.eazybytes.entities

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object EventEntity: Table<Nothing> ("event_item") {
    val id = int("id").primaryKey()
    val description = varchar("description")
    val name = varchar("name")
    val poster = varchar("poster")
    val price = varchar("price")
}