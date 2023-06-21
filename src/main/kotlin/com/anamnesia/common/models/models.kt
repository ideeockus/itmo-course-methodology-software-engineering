package com.anamnesia.common.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TimeSlotState {
    @SerialName("full")
    FULL,
    @SerialName("free")
    FREE,
}
