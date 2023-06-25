package com.anamnesia.requests

import com.anamnesia.repository.models.PatientFamiliar
import kotlinx.serialization.Serializable


@Serializable
data class AddPatientFamiliarReq(
    val patientId: Int,
    val familiar: PatientFamiliar,
)

@Serializable
data class AddPatientFamiliarResp(
    val familiarId: Int,
)