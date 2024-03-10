package com.memoryerasureservice.model

import kotlinx.serialization.Serializable

@Serializable
data class Statistic(
    val key: String,
    val value: String
)
