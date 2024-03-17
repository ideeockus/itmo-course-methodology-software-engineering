package com.memoryerasureservice.services

import com.memoryerasureservice.database.Equipment
import com.memoryerasureservice.database.Patients
import com.memoryerasureservice.database.Statistics
import com.memoryerasureservice.model.Statistic
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

class StatisticsService {
    fun getBaseStatistics(): List<Statistic> = transaction {
        Statistics.selectAll().map { toStatistic(it) }
    }

    fun getAllStatistics(): List<Statistic> = transaction {
        val stats = mutableListOf<Statistic>()

        // Количество оборудования по статусам
        Equipment
            .slice(Equipment.status, Equipment.id.count())
            .selectAll()
            .groupBy(Equipment.status)
            .forEach {
                stats.add(Statistic("EquipmentStatus_${it[Equipment.status]}", it[Equipment.id.count()].toString()))
            }

        // Количество пациентов
        val totalPatients = Patients.selectAll().count()
        stats.add(Statistic("TotalPatients", totalPatients.toString()))

        // Статистика использования оборудования (для простоты - количество оборудования)
        val totalEquipment = Equipment.selectAll().count()
        stats.add(Statistic("TotalEquipment", totalEquipment.toString()))

        // Инвентаризация оборудования, требующего обновления или замены (пример: оборудование с истекшим сроком службы)
        val expiredEquipmentCount = Equipment.selectAll().count {
            val startDate = it[Equipment.maintenanceDate] ?: LocalDate.now()
            val expiryDate = startDate.plusMonths(it[Equipment.serviceLife].toLong())
            expiryDate.isBefore(LocalDate.now())
        }
        stats.add(Statistic("ServiceLifeExceeded", expiredEquipmentCount.toString()))

        localizeStatistics(stats)
    }

    fun localizeStatistics(statistics: List<Statistic>): List<Statistic> {
        val statusMap = mapOf(
            "Available" to "Доступно",
            "Reserved" to "Зарезервировано",
            "Corrupted" to "Повреждено",
            "NotAvailable" to "Не доступно",
            "Busy" to "Занято"
        )

        val keyDescriptionMap = mapOf(
            "EquipmentStatus" to "Статус оборудования",
            "TotalPatients" to "Всего пациентов",
            "TotalEquipment" to "Всего оборудования",
            "ServiceLifeExceeded" to "Оборудование с истекшим сроком службы"
        )

        // Преобразование статистики
        return statistics.map { statistic ->
            // Разделение ключа на составляющие для обработки особых случаев, например "EquipmentStatus_Available"
            val keyParts = statistic.key.split("_")
            val localizedKey = when {
                keyParts.size > 1 && keyParts[0] == "EquipmentStatus" -> {
                    "${keyDescriptionMap["EquipmentStatus"]} '${statusMap[keyParts[1]] ?: keyParts[1]}'"
                }
                else -> {
                    keyDescriptionMap[statistic.key] ?: statistic.key
                }
            }
            Statistic(localizedKey, statistic.value)
        }
    }


    fun updateStatistic(key: String, value: String) = transaction {
        Statistics.update({ Statistics.key eq key }) {
            it[Statistics.value] = value
        }
    }

    fun addStatistic(key: String, value: String) = transaction {
        Statistics.insert {
            it[Statistics.key] = key
            it[Statistics.value] = value
        }
    }

    fun incrementStatistic(key: String, incrementBy: Long = 1) = transaction {
        val current = Statistics.select { Statistics.key eq key }
            .mapNotNull { it[Statistics.value].toLongOrNull() }
            .singleOrNull() ?: 0L

        val newValue = current + incrementBy

        if (current == 0L) {
            Statistics.insert {
                it[Statistics.key] = key
                it[Statistics.value] = newValue.toString()
            }
        } else {
            Statistics.update({ Statistics.key eq key }) {
                it[Statistics.value] = newValue.toString()
            }
        }
    }

    private fun toStatistic(row: ResultRow): Statistic =
        Statistic(
            key = row[Statistics.key],
            value = row[Statistics.value]
        )
}
