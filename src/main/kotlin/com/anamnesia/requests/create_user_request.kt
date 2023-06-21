package com.anamnesia.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateRequestReq(
    val name: String,
    val phone: String,
    val email: String,
    val policyChecked: String,
    val time: String,
    val date: String,
)

@Serializable
data class CreateRequestResp(
    val cardId: Int,
    val userToken: String,
)