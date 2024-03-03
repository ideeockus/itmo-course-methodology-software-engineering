//import com.memoryerasureservice.model.Familiar
//import com.memoryerasureservice.model.FamiliarState
//import junit.framework.TestCase.assertEquals
//import org.jetbrains.exposed.sql.transactions.transaction
//import org.junit.Test
//
//class FamiliarServiceTest : DatabaseTest() {
//
//    private val service = FamiliarService()
//
//    @Test
//    fun `test add familiar`() {
//        val newFamiliar = Familiar(
//            id = 0, // ID будет присвоен после вставки в БД
//            name = "John Doe",
//            phone = "123456789",
//            email = "john.doe@example.com",
//            homePhone = null,
//            workPhone = null,
//            state = FamiliarState.NotProcessed
//        )
//
//        val addedFamiliar = transaction { service.addFamiliar(newFamiliar) }
//
//        assertEquals("John Doe", addedFamiliar.name)gi
//        assertEquals("123456789", addedFamiliar.phone)
//        // Другие проверки
//    }
//}
