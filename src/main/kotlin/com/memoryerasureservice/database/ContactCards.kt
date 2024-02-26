package com.memoryerasureservice.database

import org.jetbrains.exposed.dao.id.IntIdTable

object ContactCards : IntIdTable() {
    val patientId = reference("patient_id", Patients)
    val name = varchar("name", 255)
    val phone = varchar("phone", 50).nullable()
    val email = varchar("email", 255).nullable()
    val status = varchar("status", 50) // Например, "не обработан", "обработан неуспешно", "оповещён"
}
