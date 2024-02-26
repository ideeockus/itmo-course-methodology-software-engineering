package com.memoryerasureservice.database

import org.jetbrains.exposed.dao.id.IntIdTable

object Statistics : IntIdTable() {
    val key = varchar("key", 255)
    val value = varchar("value", 255)
}
