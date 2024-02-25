package com.memoryerasureservice.services

import com.memoryerasureservice.database.ContactAttempts
import com.memoryerasureservice.database.ContactCards
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.transactions.transaction

class ContactService {

    fun addContactAttempt(contactCardId: Int, method: String, result: String) {
        transaction {
            // Добавление записи о попытке связи
            ContactAttempts.insert {
                it[this.contactCardId] = contactCardId
                it[this.attemptDate] = java.time.LocalDateTime.now()
                it[this.method] = method
                it[this.result] = result
            }
            // Обновление статуса контактной карточки в зависимости от результата попытки
            val newStatus = if (result == "успешно") "оповещён" else "обработан неуспешно"
            updateContactCardStatus(contactCardId, newStatus)
        }
    }

    private fun updateContactCardStatus(contactCardId: Int, newStatus: String) {
        // Обновление статуса контактной карточки
        ContactCards.update({ ContactCards.id eq contactCardId }) {
            it[status] = newStatus
        }
    }

    fun getUnnotifiedContacts(): List<ContactInfo> = transaction {
        // Получение списка контактов со статусом отличным от “оповещён”
        ContactCards.slice(ContactCards.id, ContactCards.name, ContactCards.phone, ContactCards.email, ContactCards.status)
            .select { ContactCards.status neq "оповещён" }
            .map { ContactInfo(
                id = it[ContactCards.id].value,
                name = it[ContactCards.name],
                phone = it[ContactCards.phone],
                email = it[ContactCards.email],
                status = it[ContactCards.status]
            )
            }
    }
}

data class ContactInfo(
    val id: Int,
    val name: String,
    val phone: String?,
    val email: String?,
    val status: String
)
