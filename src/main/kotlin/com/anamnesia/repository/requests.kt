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
    url = "jdbc:sqlite:database.db",
    driver = "org.sqlite.JDBC",
    user = "root",
//    driver = "org.h2.Driver",
    password = ""
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
fun create_user_request(req: CreateRequestReq): Int {
    val patient_repo = PatientCardRepository(database)

    val new_card = PatientCard(req.name, 0)

    return runBlocking {
        val new_card_id = patient_repo.create(new_card)
        return@runBlocking new_card_id
    }
}

