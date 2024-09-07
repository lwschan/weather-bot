package dev.lewischan.weatherbot.domain

import java.time.Instant

data class User(
    val id: Long,
    val externalPlatform: ExternalPlatform,
    val externalUserId: Long,
    val createdOn: Instant
)
