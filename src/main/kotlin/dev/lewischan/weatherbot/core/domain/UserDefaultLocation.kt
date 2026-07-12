package dev.lewischan.weatherbot.core.domain

import dev.lewischan.weatherbot.locations.Location

data class UserDefaultLocation(
    val id: Long,
    val userId: Long,
    val location: Location
)
