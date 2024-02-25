package com.memoryerasureservice.database

import org.jetbrains.exposed.dao.id.IntIdTable

object Doctors : IntIdTable() {
    val name = varchar("name", 255)
    val specialty = varchar("specialty", 255)
}
