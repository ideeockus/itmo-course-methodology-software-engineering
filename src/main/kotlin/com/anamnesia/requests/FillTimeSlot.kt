package com.anamnesia.requests

import kotlinx.serialization.Serializable

@Serializable
data class FillTimeSlotReq(
    val doctorId: Int,
    val time: String,
    val date: String,
)

@Serializable
data class FillTimeSlotResp(
    val status: Boolean
)