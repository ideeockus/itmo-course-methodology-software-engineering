package com.anamnesia.requests

import com.anamnesia.repository.models.FamiliarAction
import kotlinx.serialization.Serializable

@Serializable
data class AddFamiliarTransactionReq(
    val familiarId: Int,
    val timestamp: Long,
    val action: FamiliarAction,
)

@Serializable
data class AddFamiliarTransactionResp(
    val status: Boolean
)