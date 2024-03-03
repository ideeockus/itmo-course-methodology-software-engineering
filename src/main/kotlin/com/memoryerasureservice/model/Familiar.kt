package com.memoryerasureservice.model

import kotlinx.serialization.Serializable


@Serializable
enum class FamiliarState {
    NotifiedSuccessful,
    NotProcessed,
    FailedToNotify,
    ProcessedUnsuccessful,
}

@Serializable
data class Familiar(
    val id: Int,
    val name: String,
    val phone: String,
    val email: String?,
    val homePhone: String?,
    val workPhone: String?,
    val state: FamiliarState,
)

// transactions

enum class FamiliarAction {
    PHONE_CALL,
    HOME_VISIT,
    CALL_EMPLOYER,
    WORK_VISIT,
    NOTIFIED,
}

@Serializable
data class FamiliarTransaction(
    val familiarId: Int,
    val timestamp: Long,
    val action: FamiliarAction,
)
