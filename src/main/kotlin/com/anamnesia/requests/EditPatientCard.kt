package com.anamnesia.requests

import com.anamnesia.repository.models.PatientCard
import kotlinx.serialization.Serializable

@Serializable
data class EditPatientCardReq(
    val patientId: Int,
//    val patientToken: String,
    val patientCard: PatientCard,
)

@Serializable
data class EditPatientCardResp(
    val updatedPatientCard: PatientCard?,
)
