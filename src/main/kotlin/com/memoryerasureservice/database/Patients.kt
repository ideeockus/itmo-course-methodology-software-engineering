package com.memoryerasureservice.database

import com.memoryerasureservice.database.Patients.nullable
import com.memoryerasureservice.model.PatientState
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object Patients : IntIdTable() {
    val name = varchar("name", 255)
    val phone = varchar("phone", 20)
    val email = varchar("email", 255).nullable()
    val appointmentDate = datetime("appointment_date")

    val age = integer("age").nullable()
    val address = varchar("address", 255).nullable()
    // todo work address

    val memoryScanId = uuid("memory_scan_id").nullable()
    val erasureSessionId = uuid("erasure_session_id").nullable()

    val state = enumerationByName("state", 25, PatientState::class)
    val userToken = uuid("user_token").nullable()
}

object PatientFamiliarRelationTable : Table() {
    val familiar = reference("familiar_id", Familiars)
    val patient = reference("patient_id", Patients)
}
