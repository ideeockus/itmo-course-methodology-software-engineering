import com.memoryerasureservice.database.Equipment
import com.memoryerasureservice.database.Patients
import com.memoryerasureservice.database.Statistics
import com.memoryerasureservice.services.StatisticsService
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue
import kotlin.test.assertNotNull

class StatisticsServiceTest {

    private val statisticsService = StatisticsService()

    @BeforeEach
    fun setUp() {
        // Настройка подключения к базе данных H2 для тестирования
        Database.connect(
            "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;",
            driver = "org.h2.Driver"
        )

        transaction {
            // Создание схемы базы данных (таблиц) в памяти
            SchemaUtils.create(Statistics, Equipment, Patients)
        }
    }

    @Test
    fun `getBaseStatistics returns non-empty list`() {
        // Этот тест предполагает, что база данных уже содержит некоторые статистические данные.
        // Реальное взаимодействие с базой данных не тестируется.
        val result = statisticsService.getBaseStatistics()

        // Проверяем, что результат не пустой
        assertTrue(result.isNotEmpty(), "Base statistics should not be empty")
    }

    @Test
    fun `getBaseStatistics returns expected statistics`() {
        transaction {
            // Добавление тестовых данных в таблицу Statistics
            Statistics.batchInsert(listOf("TotalPatients", "TotalEquipment")) { key ->
                this[Statistics.key] = key
                this[Statistics.value] = "1" // Просто для примера
            }
        }

        val result = statisticsService.getBaseStatistics()

        // Проверяем, что полученные данные соответствуют ожиданиям
        assertNotNull(result.find { it.key == "TotalPatients" && it.value == "1" })
        assertNotNull(result.find { it.key == "TotalEquipment" && it.value == "1" })
    }
}
