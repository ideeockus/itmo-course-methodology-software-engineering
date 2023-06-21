package com.anamnesia.requests

import com.anamnesia.common.models.TimeSlotState
import kotlinx.serialization.Serializable


@Serializable
data class GetTimeSlotsReq(
    val date: String,
    val doctorId: Int,
)

@Serializable
data class GetTimeSlotsResp(
    val timeslots: HashMap<String, TimeSlotState>,
)