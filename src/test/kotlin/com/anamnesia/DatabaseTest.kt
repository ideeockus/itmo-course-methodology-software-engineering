//import org.jetbrains.exposed.sql.Database
//import org.jetbrains.exposed.sql.transactions.transaction
//import org.jetbrains.exposed.sql.SchemaUtils
//
//open class DatabaseTest {
////    @BeforeEach
//    fun setup() {
//        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
////        transaction {
////            SchemaUtils.create(Familiars, Patients, PatientFamiliarRelationTable /* Другие таблицы */)
////        }
//    }
//
////    @AfterEach
////    fun teardown() {
////        transaction {
////            SchemaUtils.drop(Familiars, Patients, PatientFamiliarRelationTable /* Другие таблицы */)
////        }
////    }
//}
