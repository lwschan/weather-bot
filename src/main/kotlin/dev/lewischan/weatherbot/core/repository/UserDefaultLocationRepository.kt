package dev.lewischan.weatherbot.core.repository

import dev.lewischan.weatherbot.core.domain.UserDefaultLocation
import dev.lewischan.weatherbot.core.location.Location

interface UserDefaultLocationRepository {

    fun findByUserId(userId: Long): UserDefaultLocation?

    fun save(userId: Long, location: Location): UserDefaultLocation

    fun deleteForUser(userId: Long)
}
