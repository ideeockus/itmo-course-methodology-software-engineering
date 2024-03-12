import com.memoryerasureservice.api.ApplyRequest
import com.memoryerasureservice.api.CreatePatientAppointment
import com.memoryerasureservice.database.PatientFamiliarRelationTable
import com.memoryerasureservice.database.Patients
import com.memoryerasureservice.model.Familiar
import com.memoryerasureservice.model.FamiliarState
import com.memoryerasureservice.model.Patient
import com.memoryerasureservice.model.PatientState
import com.memoryerasureservice.services.PatientService

import io.mockk.every
import io.mockk.mockk
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insertAndGetId
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDateTime
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import java.util.*

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class PatientServiceTest {
    @BeforeEach
    fun setup() {
        // Настройка подключения к тестовой базе данных H2
        Database.connect(
            "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;",
            driver = "org.h2.Driver"
        )

        transaction {
            SchemaUtils.create(Patients, PatientFamiliarRelationTable)
            // И другие необходимые действия по подготовке среды
        }
    }

    @Test
    fun `createPatient adds new patient to database`() {
        val service = PatientService()
        val newPatient = service.createPatient("Test Name", "12345", "test@example.com", LocalDateTime.now())

        assertNotNull(newPatient)
        // Проверки, что пациент был добавлен в базу данных
    }

    @Test
    @Order(1)
    fun `getAllPatients returns all patients from database`() {
        // Предварительное заполнение тестовой базы данных данными о пациентах
        transaction {
            Patients.insert {
                it[name] = "John Doe"
                it[phone] = "1234567890"
                it[email] = "john@example.com"
                it[appointmentDate] = LocalDateTime.now()
                it[state] = PatientState.Stage1
                // Заполните другие поля по необходимости
            }
            Patients.insert {
                it[name] = "Jane Doe"
                it[phone] = "0987654321"
                it[email] = "jane@example.com"
                it[appointmentDate] = LocalDateTime.now().plusDays(1)
                it[state] = PatientState.Stage1
                // Заполните другие поля по необходимости
            }
        }

        val patientService = PatientService()
        val patients = patientService.getAllPatients()

        // Проверка количества возвращенных записей
        assertEquals(2, patients.size, "Должно быть возвращено 2 пациента")

        // Проверка конкретных значений для пациентов
        assertEquals("John Doe", patients[0].name)
        assertEquals("1234567890", patients[0].phone)
        assertEquals("john@example.com", patients[0].email)

        assertEquals("Jane Doe", patients[1].name)
        assertEquals("0987654321", patients[1].phone)
        assertEquals("jane@example.com", patients[1].email)
    }

    @Test
    fun `getPatientById returns correct patient for given id`() {
        // Добавляем пациента в тестовую базу данных
        val expectedId = transaction {
            Patients.insertAndGetId {
                it[name] = "John Doe"
                it[phone] = "1234567890"
                it[email] = "john@example.com"
                it[appointmentDate] = LocalDateTime.now()
                it[state] = PatientState.Stage1
            }.value
        }

        // Вызов метода, который мы тестируем
        val result = PatientService().getPatientById(expectedId)

        // Проверки
        assertNotNull(result)
        assertEquals("John Doe", result.name)
        assertEquals("1234567890", result.phone)
        assertEquals("john@example.com", result.email)
    }

    @Test
    fun `createPatient successfully creates a new patient and returns it`() {
        val name = "Alice Wonderland"
        val phone = "555-1234"
        val email = "alice@example.com"
        val appointmentDateTime = LocalDateTime.now()

        transaction {
            // Предполагаем, что этот метод добавляет запись в базу данных и возвращает объект пациента
            val createdPatient = PatientService().createPatient(name, phone, email, appointmentDateTime)
            assertNotNull(createdPatient)
            assertEquals(name, createdPatient.name)
            assertEquals(phone, createdPatient.phone)
            assertEquals(email, createdPatient.email)
            assertEquals(appointmentDateTime, createdPatient.appointmentDate)
        }
    }

    @Test
    fun `applyForService creates a new patient from request`() {
        val request = ApplyRequest(
            name = "Bob Builder",
            phone = "555-6789",
            email = "bob@example.com",
            appointmentDate = "2023-01-01 10:00:00"
        )

        transaction {
            val createdPatient = PatientService().applyForService(request)
            assertNotNull(createdPatient)
            assertEquals(request.name, createdPatient.name)
            assertEquals(request.phone, createdPatient.phone)
            assertEquals(request.email, createdPatient.email)
            // Проверяем дату и время назначения
        }
    }

    @Test
    fun `createAppointment schedules an appointment and returns patient data`() {
        val request = CreatePatientAppointment(
            name = "Charlie Day",
            phone = "555-0000",
            email = "charlie@example.com",
            appointmentDate = "2023-01-02 ",
            appointmentTime = "15:00",
            policyChecked = true,
        )

        transaction {
            val patient = PatientService().createAppointment(request)
            assertNotNull(patient)
            assertEquals(request.name, patient.name)
            assertEquals(request.phone, patient.phone)
            assertEquals(request.email, patient.email)
            // Проверяем дату и время назначения и токен пользователя
        }
    }

    @Test
    fun `getPatientByToken returns correct patient for given token`() {
        val token = UUID.randomUUID()
        // Предварительно создайте пациента с этим токеном в базе данных

        val expectedId = transaction {
            Patients.insertAndGetId {
                it[name] = "John Doe"
                it[phone] = "1234567890"
                it[email] = "john@example.com"
                it[appointmentDate] = LocalDateTime.now()
                it[state] = PatientState.Stage1
                it[userToken] = token
            }.value
        }

        val patient = PatientService().getPatientByToken(token)
        assertNotNull(patient)
        assertEquals(token, patient.userToken)
    }

    @Test
    fun `updatePatient updates existing patient details`() {
        val patientId = 1 // Предполагаем, что пациент с этим ID уже существует
        val expectedId = PatientService().createPatient(
            "First Name",
            "98398492",
            "email",
            LocalDateTime.now(),
        )

        val updatedData = Patient(
            patientId,
            "Updated Name",
            "New Phone",
            "newemail@example.com",
            LocalDateTime.now(),
            20,
            "asd",
            null,
            null,
            emptyList(),
            PatientState.Stage1,
            null,
        )

        transaction {
            val updatedPatient = PatientService().updatePatient(patientId, updatedData)
            assertNotNull(updatedPatient)
            assertEquals(updatedData.name, updatedPatient?.name)
            assertEquals(updatedData.phone, updatedPatient?.phone)
            assertEquals(updatedData.email, updatedPatient?.email)
        }
    }

    @Test
    fun `addFamiliarToPatient links a familiar to a patient`() {
        val patientId = 1 // ID существующего пациента
        val familiarData = Familiar(
            id = 1,
            name = "Familiar Name",
            homePhone = "555-1111",
            email = "asd",
            homeAddress = "homeaddr",
            workAddress = "workAddr",
            workPhone = "9238492",
            state = FamiliarState.NotifiedSuccessful,
        )

        val familiar = PatientService().addFamiliarToPatient(patientId, familiarData)
        assertNotNull(familiar)
        // Дополнительные проверки свойств добавленного родственника
    }


}
