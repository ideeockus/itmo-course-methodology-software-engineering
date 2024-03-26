//import com.memoryerasureservice.model.UserRole
//import com.memoryerasureservice.services.UsersService
//import io.mockk.every
//import io.mockk.mockkStatic
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.mindrot.jbcrypt.BCrypt
//import kotlin.test.assertEquals
//import kotlin.test.assertFalse
//import kotlin.test.assertTrue
//
//
//class UsersServiceTest {
//
//    private lateinit var usersService: UsersService
//
//    @BeforeEach
//    fun setUp() {
//        usersService = UsersService()
//        mockkStatic(BCrypt::class)
//    }
//
//    @Test
//    fun `createUser creates a user successfully`() {
//        val username = "testUser"
//        val password = "testPass"
//        val role = UserRole.TECHNICIAN
//        val passwordHash = "hashedTestPass"
//        val userId = 1
//
//        // Мокируем BCrypt
//        every { BCrypt.hashpw(password, any()) } returns passwordHash
//
//        // Предполагаем, что ID пользователя успешно возвращается после вставки
//        // Заметьте, это очень упрощенное представление. В реальности потребуется мокирование transaction блока
//        mockkStatic("org.jetbrains.exposed.sql.transactions.TransactionKt") // Статический мок transaction блока
//
//        val result = usersService.createUser(username, password, role)
//
//        assertEquals(userId, result?.id)
//        assertEquals(username, result?.name)
//        assertEquals(passwordHash, result?.passwordHash)
//        assertEquals(role, result?.role)
//    }
//
//    @Test
//    fun `authenticateUser authenticates user successfully`() {
//        val username = "testUser"
//        val password = "testPass"
//        val passwordHash = "hashedTestPass"
//
//        every { BCrypt.checkpw(password, passwordHash) } returns true
//        // Аналогично мокированию `transaction` для получения пользователя
//
//        val result = usersService.authenticateUser(username, password)
//
//        assertFalse(result)
//    }
//}
