package com.anamnesia.repository

import com.anamnesia.plugins.UserService
import com.anamnesia.repository.models.PatientCard
import com.anamnesia.repository.models.PatientCardRepository
import com.anamnesia.requests.CreateRequestReq
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlinx.coroutines.runBlocking



val database = Database.connect(
//    url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
//    url = "jdbc:sqlite:database.db",
    url = "jdbc:postgresql://localhost:5432/mse_lab_db",
    driver = "org.sqlite.JDBC",
    user = "mse_lab_user",
//    driver = "org.h2.Driver",
    password = "pass123",
)


//fun main() {


//    Database.connect("jdbc:sqlite:test.db", driver = "org.sqlite.JDBC")

//    transaction {
//        SchemaUtils.create(Users)
//
//        Users.insert {
//            it[name] = "John Doe"
//        }
//
//        for (user in Users.selectAll()) {
//            println("${user[Users.name]}")
//        }
//    }
//}

//object Users : Table() {
//    val id = integer("id").autoIncrement().primaryKey()
//    val name = varchar("name", length = 50)
//}
fun createPatientCard(name: String, phone: String, token: String): Int {
    val patientRepo = PatientCardRepository(database)

    val newCard = PatientCard(name, null, phone, null, token)

    return runBlocking {
        val newCardId = patientRepo.create(newCard)
        return@runBlocking newCardId
    }
}

