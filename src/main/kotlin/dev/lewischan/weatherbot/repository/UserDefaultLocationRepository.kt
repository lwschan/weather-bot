package dev.lewischan.weatherbot.repository

import dev.lewischan.weatherbot.domain.UserDefaultLocation
import dev.lewischan.weatherbot.model.Location

interface UserDefaultLocationRepository {

    fun findByUserId(userId: Long): UserDefaultLocation?

    fun save(userId: Long, location: Location): UserDefaultLocation

    fun deleteForUser(userId: Long)
}