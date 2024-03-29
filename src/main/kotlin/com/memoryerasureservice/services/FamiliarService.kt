import com.memoryerasureservice.database.Familiars
import com.memoryerasureservice.database.PatientFamiliarRelationTable
import com.memoryerasureservice.model.Familiar
import com.memoryerasureservice.model.FamiliarState
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class FamiliarService {

    fun getAllFamiliars(): List<Familiar> = transaction {
        Familiars.selectAll().map { toFamiliar(it) }
    }

    fun getFamiliar(id: Int): Familiar? = transaction {
        Familiars.select { Familiars.id eq id }
            .mapNotNull { toFamiliar(it) }
            .singleOrNull()
    }

    fun addFamiliar(familiar: Familiar): Familiar = transaction {
        val insertedId = Familiars.insertAndGetId {
            it[name] = familiar.name
            it[email] = familiar.email
            it[homePhone] = familiar.homePhone
            it[workPhone] = familiar.workPhone
            it[state] = familiar.state
        }
        getFamiliar(insertedId.value)!!
    }

    fun updateFamiliar(id: Int, familiar: Familiar): Familiar? = transaction {
        Familiars.update({ Familiars.id eq id }) {
            it[name] = familiar.name
            it[email] = familiar.email
            it[homePhone] = familiar.homePhone
            it[workPhone] = familiar.workPhone
            it[homeAddress] = familiar.homeAddress
            it[workAddress] = familiar.workAddress
            it[state] = familiar.state
        }
        getFamiliar(id)
    }

    fun deleteFamiliar(id: Int): Boolean = transaction {
        Familiars.deleteWhere { Familiars.id eq id } > 0
    }

    fun getFamiliarsForPatient(patientId: Int): List<Familiar> = transaction {
        // Создаем запрос на выборку всех записей из Familiars, которые связаны с указанным patientId
        val familiarIds = PatientFamiliarRelationTable
            .select { PatientFamiliarRelationTable.patient eq patientId }
            .map { it[PatientFamiliarRelationTable.familiar] }

        // Выбираем всех родственников по их ID
        Familiars.select { Familiars.id inList familiarIds }.map { toFamiliar(it) }
    }

    fun setNextFamiliarStatus(familiarId: Int): FamiliarState = transaction {
        val currentFamiliar =
            Familiars.select { Familiars.id eq familiarId }.firstOrNull() ?: return@transaction FamiliarState.NotProcessed
        val currentState = currentFamiliar[Familiars.state]
        val nextState = currentState.ordinal.let { ordinal ->
            if (ordinal < FamiliarState.values().lastIndex) FamiliarState.values()[ordinal + 1] else FamiliarState.NotifiedSuccessful
        }

        Familiars.update({ Familiars.id eq familiarId }) {
            it[state] = nextState
        }
        nextState
    }


        private fun toFamiliar(row: ResultRow): Familiar =
        Familiar(
            id = row[Familiars.id].value,
            name = row[Familiars.name],
            email = row[Familiars.email],
            homePhone = row[Familiars.homePhone],
            workPhone = row[Familiars.workPhone],
            homeAddress = row[Familiars.homeAddress],
            workAddress = row[Familiars.workAddress],
            state = row[Familiars.state]
        )
}
