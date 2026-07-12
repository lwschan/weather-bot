package dev.lewischan.weatherbot.core.service

import dev.lewischan.weatherbot.core.domain.UserDefaultLocation
import dev.lewischan.weatherbot.locations.Location
import dev.lewischan.weatherbot.core.repository.UserDefaultLocationRepository
import org.springframework.stereotype.Service

@Service
class UserDefaultLocationService(
    private val userDefaultLocationRepository: UserDefaultLocationRepository
) {

    fun findByUserId(userId: Long): UserDefaultLocation? = userDefaultLocationRepository.findByUserId(userId)

    fun save(userId: Long, location: Location): UserDefaultLocation = userDefaultLocationRepository.save(userId, location)

    fun deleteForUser(userId: Long) = userDefaultLocationRepository.deleteForUser(userId)

}
