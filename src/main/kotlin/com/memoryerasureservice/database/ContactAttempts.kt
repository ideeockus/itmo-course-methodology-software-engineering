package com.memoryerasureservice.database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object ContactAttempts : IntIdTable() {
    val contactCardId = reference("contact_card_id", ContactCards)
    val attemptDate = datetime("attempt_date")
    val method = varchar("method", 255) // Например, "звонок", "посещение"
    val result = varchar("result", 255) // Например, "успешно", "не удалось связаться"
}
