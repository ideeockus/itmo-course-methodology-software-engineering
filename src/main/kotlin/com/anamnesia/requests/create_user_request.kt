package com.anamnesia.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateRequestReq(
    val name: String,
    val phone: String,
    val email: String,
    val policy_checked: Boolean = false,
)

@Serializable
data class CreateRequestResp(
    val cardId: Int,
)