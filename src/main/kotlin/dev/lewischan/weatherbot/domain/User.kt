package dev.lewischan.weatherbot.domain

data class User(
    val id: Long,
    val externalPlatform: ExternalPlatform,
    val externalUserId: Long
)
