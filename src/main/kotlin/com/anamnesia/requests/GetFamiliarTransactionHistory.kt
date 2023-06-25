package com.anamnesia.requests

import com.anamnesia.repository.models.FamiliarTransaction
import kotlinx.serialization.Serializable

@Serializable
data class GetFamiliarTransactionHistoryReq(
    val familiarId: Int,
)

@Serializable
data class GetFamiliarTransactionHistoryResp(
    val history: List<FamiliarTransaction>,
)