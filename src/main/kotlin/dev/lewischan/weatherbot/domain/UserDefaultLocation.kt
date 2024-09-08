package dev.lewischan.weatherbot.domain

import dev.lewischan.weatherbot.model.Location

data class UserDefaultLocation(
    val id: Long,
    val userId: Long,
    val location: Location
)
