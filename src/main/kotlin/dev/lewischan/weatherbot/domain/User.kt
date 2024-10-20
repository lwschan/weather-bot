package dev.lewischan.weatherbot.domain

import java.util.UUID

data class User(
    val id: Long,
    val externalPlatform: ExternalPlatform,
    val externalUserId: UUID
)
