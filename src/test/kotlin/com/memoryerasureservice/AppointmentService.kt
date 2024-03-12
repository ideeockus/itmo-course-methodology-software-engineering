//import com.memoryerasureservice.services.AppointmentService
//import com.memoryerasureservice.database.Appointments
//import org.jetbrains.exposed.sql.insert
//import org.jetbrains.exposed.sql.transactions.TransactionManager
//import org.jetbrains.exposed.sql.transactions.transaction
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.extension.ExtendWith
//import org.mockito.junit.jupiter.MockitoExtension
//import org.mockito.kotlin.mock
//import org.mockito.kotlin.verify
//import org.mockito.kotlin.whenever
//import kotlin.test.assertEquals
//import java.time.LocalDateTime
//
//@ExtendWith(MockitoExtension::class)
//class AppointmentServiceTest {
//
//    private lateinit var appointmentService: AppointmentService
//    // Мокаем зависимости
//    private val transactionManager: TransactionManager = mock()
//
//    @BeforeEach
//    fun setUp() {
//        // Инициализируем сервис с моковыми зависимостями
//        appointmentService = AppointmentService()
//    }
//
//    @Test
//    fun `scheduleAppointment schedules an appointment correctly`() {
//        // Подготавливаем входные данные и ожидаемый результат
//        val patientId = 1
//        val doctorId = 1
//        val dateTime = LocalDateTime.now()
//
//        // Настройка мока для транзакции
//        whenever(transactionManager.transaction { any<() -> Any>() }).thenAnswer { invocation ->
//            (invocation.arguments[0] as () -> Any).invoke()
//        }
//
//        // Вызываем метод для тестирования
//        val result = appointmentService.scheduleAppointment(patientId, doctorId, dateTime)
//
//        // Проверяем результат
//        verify(transactionManager).transaction(any())
//        // Здесь должен быть код для проверки результата. Например, проверить была ли вставка в базу.
//        // Это может потребовать дополнительной настройки моков, специфичных для вашей логики базы данных.
//    }
//}
