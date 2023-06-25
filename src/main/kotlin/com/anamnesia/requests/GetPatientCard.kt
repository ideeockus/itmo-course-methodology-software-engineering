package com.anamnesia.requests

import com.anamnesia.repository.models.PatientCard
import kotlinx.serialization.Serializable


@Serializable
data class GetPatientCardReq(
//    val patientId: Int,
    val patientToken: String,
)

@Serializable
data class GetPatientCardResp(
    val patientCard: PatientCard?,
)